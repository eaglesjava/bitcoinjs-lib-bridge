package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
@PerActivity
public class BtcAddressPresenter<M extends WalletModel, V extends BtcAddressMvpView> extends ModelPresenter<M, V> implements BtcAddressMvpPresentder<M, V> {
    @Inject
    public BtcAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void newAddress() {
        if (!isValidWallet()) {
            return;
        }
        //刷新地址index+1
        Wallet wallet = getMvpView().getWallet();
        long index = wallet.getLastAddressIndex() + 1;
        wallet.setLastAddressIndex(index);
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(wallet.getXPublicKey(), index, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().newAddressFail();
                    return;
                }
                String address = jsResult[0];

                if (!isValidBtcAddress(address)) {
                    return;
                }
                wallet.setLastAddress(address);
                //更新地址index 并请求后台刷新
                updateAddressIndex(wallet);

            }
        });
    }

    private void updateAddressIndex(Wallet wallet) {
        wallet.setLastAddressIndex(wallet.getLastAddressIndex());
        getCompositeDisposable().add(getModelManager().updateWallet(wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (aBoolean) {
                            //后台扫描地址
                            requestRefreshAddress(wallet);
                        } else {
                            //本地index更新失败
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().newAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().newAddressFail();
                    }
                }));
    }

    private void requestRefreshAddress(Wallet wallet) {
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(wallet.getXPublicKey());
        // check xPublicKeyHash
        if (!isValidXPublicKeyHash(xPublicKeyHash)) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .refreshAddress(new RefreshAddressRequest(xPublicKeyHash, wallet.getLastAddressIndex()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<RefreshAddressResponse>>() {
                    @Override
                    public void onNext(ApiResponse<RefreshAddressResponse> refreshAddressResponseApiResponse) {
                        super.onNext(refreshAddressResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (refreshAddressResponseApiResponse != null && refreshAddressResponseApiResponse.isSuccess()) {

                            getMvpView().newAddressSuccess(wallet.getLastAddress());
                        } else {
                            getMvpView().newAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        } // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
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

    public boolean isValidXPublicKeyHash(String hash) {
        if (StringUtils.isEmpty(hash)) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

    private boolean isValidBtcAddress(String btcAddress) {
        if (StringUtils.isEmpty(btcAddress)) {
            getMvpView().newAddressFail();
            return false;
        }
        return true;
    }

}
