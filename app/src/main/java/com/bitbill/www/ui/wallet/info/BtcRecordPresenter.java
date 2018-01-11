package com.bitbill.www.ui.wallet.info;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.GetTxListRequest;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
@PerActivity
public class BtcRecordPresenter<M extends TxModel, V extends BtcRecordMvpView> extends ModelPresenter<M, V> implements BtcRecordMvpPresenter<M, V> {
    @Inject
    public BtcRecordPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getTxRecord() {
        if (!isValidWallet() || !isValidXPublicKey()) {
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(wallet.getXPublicKey());
        getCompositeDisposable().add(Observable.fromCallable(() -> {
            List<TxRecord> txRecordList = wallet.getTxRecordList();
            if (StringUtils.isEmpty(txRecordList)) {
                return 0l;
            }
            for (int i = txRecordList.size() - 1; i < 0; i--) {
                if (txRecordList.get(i).getHeight() == -1l) {
                    return txRecordList.get(i++).getElementId();
                }
            }
            return 0l;
        })
                .concatMap(aLong -> getModelManager().getTxList(new GetTxListRequest(xPublicKeyHash, aLong)))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<ListTxElementResponse>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<ListTxElementResponse> listTxElementResponseApiResponse) {
                        super.onNext(listTxElementResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listTxElementResponseApiResponse != null) {
                            ListTxElementResponse data = listTxElementResponseApiResponse.getData();
                            if (data != null) {
                                List<TxElement> list = data.getList();
                                getMvpView().getTxRecordSuccess(list);
                            } else {
                                getMvpView().getTxRecordFail();
                            }
                        } else {
                            getMvpView().getTxRecordFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().getTxRecordFail();
                        }
                    }
                }));
    }

    private boolean isValidWallet() {
        if (getMvpView().getWallet() == null) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

    public boolean isValidXPublicKey() {
        if (StringUtils.isEmpty(getMvpView().getWallet().getXPublicKey())) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }
}
