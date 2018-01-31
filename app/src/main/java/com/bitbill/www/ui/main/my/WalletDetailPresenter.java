package com.bitbill.www.ui.main.my;

import android.graphics.Bitmap;

import com.androidnetworking.error.ANError;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.network.entity.DeleteWalletRequest;

import javax.inject.Inject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/28.
 */
@PerActivity
public class WalletDetailPresenter<M extends WalletModel, V extends WalletDetailMvpView> extends ModelPresenter<M, V> implements WalletDetailMvpPresenter<M, V> {

    @Inject
    public WalletDetailPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    /**
     * bitbill://www.bitbill.com/contact?id=\(id)
     */
    @Override
    public void createWalletIdQrcode() {

        if (!isValidWalletId()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(AppConstants.SCHEME_BITBILL);
        stringBuilder.append(":")
                .append("//")
                .append(AppConstants.HOST_BITBILL)
                .append("/")
                .append(AppConstants.PATH_CONTACT)
                .append("?")
                .append(AppConstants.QUERY_ID)
                .append("=")
                .append(getMvpView().getWalletId());

        getCompositeDisposable().add(Observable.just(
                //生成地址二维码
                QRCodeEncoder.syncEncodeQRCode(stringBuilder.toString(), 360))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        super.onNext(bitmap);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bitmap != null) {
                            getMvpView().createQrcodeSuccess(bitmap);
                        } else {
                            getMvpView().createQrcodeFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().createQrcodeFail();
                    }
                }));


    }

    @Override
    public void deleteWallet() {
        if (!isValidWallet() || !isValidPublicKey()) {
            return;
        }
        String extentedPublicKey = getMvpView().getWallet().getExtentedPublicKey();
        String extendedKeysHash = EncryptUtils.encryptMD5ToString(extentedPublicKey);
        getCompositeDisposable().add(getModelManager()
                .deleteWallet(new DeleteWalletRequest(extendedKeysHash, DeviceUtil.getDeviceId()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        super.onNext(apiResponse);
                        if (handleApiResponse(apiResponse)) {
                            return;
                        }

                        if (apiResponse.isSuccess()) {
                            deleteLocalWallet();
                        } else {
                            getMvpView().deleteWalletFail();
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
                            getMvpView().deleteWalletFail();
                        }
                    }
                }));
    }

    public void deleteLocalWallet() {
        if (!isValidWallet()) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .deleteWallet(getMvpView().getWallet())
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {
                            getMvpView().deleteWalletSuccess();
                        } else {
                            getMvpView().deleteWalletFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().deleteWalletFail();
                    }
                }));
    }

    public boolean isValidWalletId() {
        if (StringUtils.isEmpty(getMvpView().getWalletId())) {
            getMvpView().getWalletInfoFail();
            return false;
        }
        return true;
    }

    public boolean isValidWallet() {
        if (getMvpView().getWallet() == null) {
            getMvpView().getWalletInfoFail();
            return false;
        }
        return true;
    }

    public boolean isValidPublicKey() {
        if (StringUtils.isEmpty(getMvpView().getWallet().getExtentedPublicKey())) {
            getMvpView().getWalletInfoFail();
            return false;
        }
        return true;
    }
}
