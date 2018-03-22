package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;
import android.util.Log;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.btc.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.btc.network.entity.TxElement;

import java.util.List;

import javax.inject.Inject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/25.
 */
@PerActivity
public class ScanPayPresenter<M extends BtcModel, V extends ScanPayMvpView> extends ModelPresenter<M, V> implements ScanPayMvpPresenter<M, V> {

    private static final String TAG = "ScanPayPresenter";

    @Inject
    public ScanPayPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
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
        Log.d(TAG, "createReceiveQrcode() called,receiveQrcodeStr:" + receiveQrcodeStr);

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

    @Override
    public void getTxInfo() {
        if (!isValidTxHash() && isValidBtcAddress()) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .getTxInfo(new GetTxInfoRequest(getMvpView().getTxHash()))
                .compose(applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<TxElement>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<TxElement> getTxInfoResponseApiResponse) {
                        super.onNext(getTxInfoResponseApiResponse);
                        if (handleApiResponse(getTxInfoResponseApiResponse)) {
                            return;
                        }
                        if (getTxInfoResponseApiResponse.isSuccess()) {
                            TxElement data = getTxInfoResponseApiResponse.getData();
                            if (data != null) {
                                List<TxElement.OutputsBean> outputs = data.getOutputs();
                                if (!StringUtils.isEmpty(outputs)) {
                                    for (TxElement.OutputsBean output : outputs) {
                                        if (getMvpView().getReceiveAddress().equalsIgnoreCase(output.getAddress())) {
                                            getMvpView().addressMatchTx(true, data);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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

    private boolean isValidTxHash() {
        if (StringUtils.isEmpty(getMvpView().getTxHash())) {
            getMvpView().getTxHashFail();
            return false;
        }
        return true;
    }
}
