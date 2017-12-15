package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

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
    public void reloadAddress(Wallet selectedWallet) {
        if (selectedWallet == null) {
            getMvpView().refreshAddressFail();
            return;
        }

        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(selectedWallet.getXPublicKey(), selectedWallet.getLastAddressIndex(), new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (jsResult == null) {
                    return;
                }
                String address = jsResult[0];

                if (StringUtils.isEmpty(address)) {
                    getMvpView().refreshAddressFail();
                    return;
                }
                createAddressQrcode(address);
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().setSendAddress(address);
            }
        });
    }

    @Override
    public void createAddressQrcode(String address) {
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
}
