package com.bitbill.www.ui.main.my;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.network.entity.GetTxElement;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2018/1/13.
 */
@PerActivity
public class WalletAddressPresenter<M extends TxModel, V extends WalletAddressMvpView> extends ModelPresenter<M, V> implements WalletAddressMvpPresenter<M, V> {
    @Inject
    AddressModel mAddressModel;

    @Inject
    public WalletAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void requestListUnspent() {

        if (!isValidWallet()) return;

        Wallet wallet = getMvpView().getWallet();
        String encryptMD5ToString = EncryptUtils.encryptMD5ToString(wallet.getXPublicKey());
        getCompositeDisposable().add(getModelManager()
                .getTxElement(new GetTxElement(encryptMD5ToString))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<GetTxElementResponse>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<GetTxElementResponse> listUnspentResponseApiResponse) {
                        super.onNext(listUnspentResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listUnspentResponseApiResponse != null && listUnspentResponseApiResponse.isSuccess()) {
                            GetTxElementResponse data = listUnspentResponseApiResponse.getData();
                            if (data != null) {
                                List<GetTxElementResponse.UtxoBean> unspentList = data.getUtxo();
                                if (!StringUtils.isEmpty(unspentList)) {
                                    //获取utxo成功
                                    getMvpView().getTxElementSuccess(unspentList, data.getFees());
                                } else {
                                    getMvpView().amountNoEnough();
                                }
                            } else {
                                getMvpView().getTxElementFail();
                            }

                        } else {
                            getMvpView().getTxElementFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void updateAddressBalance(List<Address> addressList) {
        if (StringUtils.isEmpty(addressList)) {
            return;
        }
        getCompositeDisposable().add(mAddressModel.updateAddressList(addressList)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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
}
