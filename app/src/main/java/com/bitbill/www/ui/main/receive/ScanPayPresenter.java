package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.BasePresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;

import javax.inject.Inject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/25.
 */
@PerActivity
public class ScanPayPresenter<V extends ScanPayMvpView> extends BasePresenter<V> implements ScanPayMvpPresenter<V> {

    @Inject
    public ScanPayPresenter(SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
    }

    @Override
    public void createReceiveQrcode() {

        String address = getMvpView().getReceiveAddress();
        if (!isValidBtcAddress()) {
            return;
        }
        //组装接受qrcode字符串

        String receiveQrcodeStr = new StringBuilder(AppConstants.SCHEME_BITCOIN)
                .append(":")
                .append(address)
                .append("?amount=")
                .append(getMvpView().getReceiveAmount()).toString();

        getCompositeDisposable().add(Observable.just(
                //生成地址二维码
                QRCodeEncoder.syncEncodeQRCode(receiveQrcodeStr, 360))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        super.onNext(bitmap);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bitmap != null) {
                            getMvpView().createReceiveQrcodeSuccess(bitmap);
                        } else {
                            getMvpView().createReceiveQrcodeFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().createReceiveQrcodeFail();
                    }
                }));
    }

    private boolean isValidBtcAddress() {
        if (StringUtils.isEmpty(getMvpView().getReceiveAddress())) {
            getMvpView().createReceiveQrcodeFail();
            return false;
        }
        return true;
    }
}
