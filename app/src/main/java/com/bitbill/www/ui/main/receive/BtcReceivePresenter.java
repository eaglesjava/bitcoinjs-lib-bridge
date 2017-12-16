package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;

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

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
@PerActivity
public class BtcReceivePresenter<M extends WalletModel, V extends BtcReceiveMvpView> extends ModelPresenter<M, V> implements BtcReceiveMvpPresenter<M, V> {
    @Inject
    public BtcReceivePresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadAddress(Wallet selectedWallet) {
        if (!isValidSelectedWallet(selectedWallet) || !isValidXPublicKey(selectedWallet)) {
            return;
        }
        // TODO: 2017/12/16 默认地址初始化钱包时直接生产地址 只需直接获取即可
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(selectedWallet.getXPublicKey(), selectedWallet.getLastAddressIndex(), new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (jsResult == null) {
                    return;
                }
                String address = jsResult[0];

                if (!isValidBtcAddress(address)) {
                    return;
                }
                selectedWallet.setLastAddress(address);

                createAddressQrcode(address);

                if (!isViewAttached()) {
                    return;
                }
                getMvpView().setSendAddress(address);
            }
        });
    }

    @Override
    public void refreshAddress(Wallet selectedWallet) {
        if (!isValidSelectedWallet(selectedWallet)) {
            return;
        }
        //刷新地址index+1
        long index = selectedWallet.getLastAddressIndex() + 1;
        selectedWallet.setLastAddressIndex(index);
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(selectedWallet.getXPublicKey(), index, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (jsResult == null) {
                    return;
                }
                String address = jsResult[0];

                if (!isValidBtcAddress(address)) {
                    return;
                }
                selectedWallet.setLastAddress(address);
                //更新地址index 并请求后台刷新
                updateAddressIndex(selectedWallet);

            }
        });
    }

    private void updateAddressIndex(Wallet selectedWallet) {
        selectedWallet.setLastAddressIndex(selectedWallet.getLastAddressIndex());
        getCompositeDisposable().add(getModelManager().updateWallet(selectedWallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (aBoolean) {
                            //后台扫描地址
                            requestRefreshAddress(selectedWallet);
                        } else {
                            //本地index更新失败
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().refreshAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().refreshAddressFail();
                    }
                }));
    }

    @Override
    public void requestRefreshAddress(Wallet selectedWallet) {
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(selectedWallet.getXPublicKey());
        // check xPublicKeyHash
        if (!isValidXPublicKeyHash(xPublicKeyHash)) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .refreshAddress(new RefreshAddressRequest(xPublicKeyHash, selectedWallet.getLastAddressIndex()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<RefreshAddressResponse>>() {
                    @Override
                    public void onNext(ApiResponse<RefreshAddressResponse> refreshAddressResponseApiResponse) {
                        super.onNext(refreshAddressResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (refreshAddressResponseApiResponse.isSuccess()) {

                            createAddressQrcode(selectedWallet.getLastAddress());
                            getMvpView().setSendAddress(selectedWallet.getLastAddress());
                            getMvpView().refreshAddressSuccess();
                        } else {
                            getMvpView().refreshAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().refreshAddressFail();
                    }
                }));
    }

    @Override
    public void createAddressQrcode(String address) {
        if (!isValidBtcAddress(address)) {
            return;
        }
        getCompositeDisposable().add(Observable.just(
                //生成地址二维码
                QRCodeEncoder.syncEncodeQRCode(address, 360))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        super.onNext(bitmap);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bitmap != null) {
                            getMvpView().createAddressQrcodeSuccess(bitmap);
                        } else {
                            getMvpView().createAddressQrcodeFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().createAddressQrcodeFail();
                    }
                }));

    }


    private boolean isValidSelectedWallet(Wallet selectedWallet) {
        if (selectedWallet == null) {
            getMvpView().getSelectedWalletFail();
            return false;
        }
        return true;
    }

    public boolean isValidXPublicKey(Wallet selectedWallet) {
        if (StringUtils.isEmpty(selectedWallet.getXPublicKey())) {
            getMvpView().getSelectedWalletFail();
            return false;
        }
        return true;
    }

    public boolean isValidXPublicKeyHash(String hash) {
        if (StringUtils.isEmpty(hash)) {
            getMvpView().refreshAddressFail();
            return false;
        }
        return true;
    }

    private boolean isValidBtcAddress(String btcAddress) {
        if (StringUtils.isEmpty(btcAddress)) {
            getMvpView().refreshAddressFail();
            return false;
        }
        return true;
    }

}
