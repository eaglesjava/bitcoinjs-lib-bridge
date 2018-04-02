package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerService;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.network.entity.GetTxListRequest;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
@PerService
public class BtcTxPresenter<M extends TxModel, V extends BtcTxMvpView> extends ModelPresenter<M, V> implements BtcTxMvpPresenter<M, V> {
    @Inject
    WalletModel mWalletModel;

    @Inject
    public BtcTxPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void requestTxRecord(Wallet wallet) {
        if (!isValidWallet(wallet) || !isValidXPublicKey(wallet)) {
            return;
        }
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(wallet.getExtentedPublicKey());
        getCompositeDisposable().add(getModelManager().getTxList(new GetTxListRequest(xPublicKeyHash, 0l))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<ListTxElementResponse>>() {
                    @Override
                    public void onNext(ApiResponse<ListTxElementResponse> listTxElementResponseApiResponse) {
                        super.onNext(listTxElementResponseApiResponse);
                        if (handleApiResponse(listTxElementResponseApiResponse)) {
                            return;
                        }
                        ListTxElementResponse data = listTxElementResponseApiResponse.getData();
                        if (data != null) {
                            List<TxElement> list = data.getList();
                            getMvpView().getTxRecordSuccess(list, wallet.getId());
                        } else {
                            getMvpView().getTxRecordFail(wallet.getId());
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
                            getMvpView().getTxRecordFail(wallet.getId());
                        }
                    }
                }));
    }

    @Override
    public void listUnconfirm() {

        getCompositeDisposable().add(mWalletModel.getAllWallets()
                .concatMap(wallets -> {

                    String extendedKeysHash = "";
                    for (int i = 0; i < wallets.size(); i++) {
                        // check xpubkey
                        String xPublicKey = wallets.get(i).getExtentedPublicKey();
                        extendedKeysHash += StringUtils.isEmpty(xPublicKey) ? "" : EncryptUtils.encryptMD5ToString(xPublicKey);
                        if (i < wallets.size() - 1) {
                            extendedKeysHash += "|";
                        }
                    }
                    return getModelManager().listUnconfirm(new ListUnconfirmRequest(extendedKeysHash));

                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<ListTxElementResponse>>() {
                    @Override
                    public void onNext(ApiResponse<ListTxElementResponse> listApiResponse) {
                        super.onNext(listApiResponse);
                        if (handleApiResponse(listApiResponse)) {
                            return;
                        }
                        if (listApiResponse.isSuccess()) {
                            ListTxElementResponse data = listApiResponse.getData();
                            if (data != null) {
                                getMvpView().listUnconfirmSuccess(StringUtils.removeDuplicateList(data.getList()));
                            } else {
                                getMvpView().listUnconfirmFail();
                            }

                        } else {
                            getMvpView().listUnconfirmFail();
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
                            getMvpView().listUnconfirmFail();
                        }

                    }
                })
        );

    }

    private boolean isValidWallet(Wallet wallet) {
        if (wallet == null) {
            getMvpView().getWalletFail(wallet.getId());
            return false;
        }
        return true;
    }

    public boolean isValidXPublicKey(Wallet wallet) {
        if (StringUtils.isEmpty(wallet.getExtentedPublicKey())) {
            getMvpView().getWalletFail(wallet.getId());
            return false;
        }
        return true;
    }
}
