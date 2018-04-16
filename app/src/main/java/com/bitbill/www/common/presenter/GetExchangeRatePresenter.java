package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerService;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.network.entity.GetExchangeRateResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/29.
 */
@PerService
public class GetExchangeRatePresenter<M extends AppModel, V extends GetExchangeRateMvpView> extends ModelPresenter<M, V> implements GetExchangeRateMvpPresenter<M, V> {

    private volatile boolean isAppBackground;

    @Inject
    public GetExchangeRatePresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }


    @Override
    public void getExchangeRate() {
        getCompositeDisposable().add(getModelManager().getExchangeRate()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<GetExchangeRateResponse>>() {
                    @Override
                    public void onNext(ApiResponse<GetExchangeRateResponse> getExchangeRateResponseApiResponse) {
                        super.onNext(getExchangeRateResponseApiResponse);
                        if (getExchangeRateResponseApiResponse != null) {
                            if (getExchangeRateResponseApiResponse.isSuccess()) {
                                GetExchangeRateResponse data = getExchangeRateResponseApiResponse.getData();
                                if (data != null) {
                                    getApp().setBtcCnyValue(data.getCnyrate());
                                    getApp().setBtcUsdValue(data.getUsdrate());
                                    if (!isViewAttached()) {
                                        return;
                                    }
                                    getMvpView().getBtcRateSuccess(data.getCnyrate(), data.getUsdrate());
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

    @Override
    public void refreshExchangeRate() {

        getCompositeDisposable().add(Observable.interval(10, 30, TimeUnit.SECONDS)
                .filter(aLong -> !isAppBackground)
                .concatMap(aLong -> getModelManager().getExchangeRate())
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<GetExchangeRateResponse>>() {
                    @Override
                    public void onNext(ApiResponse<GetExchangeRateResponse> getExchangeRateResponseApiResponse) {
                        super.onNext(getExchangeRateResponseApiResponse);
                        if (getExchangeRateResponseApiResponse != null) {
                            if (getExchangeRateResponseApiResponse.isSuccess()) {
                                GetExchangeRateResponse data = getExchangeRateResponseApiResponse.getData();
                                if (data != null) {
                                    getApp().setBtcCnyValue(data.getCnyrate());
                                    getApp().setBtcUsdValue(data.getUsdrate());
                                    if (!isViewAttached()) {
                                        return;
                                    }
                                    getMvpView().getBtcRateSuccess(data.getCnyrate(), data.getUsdrate());
                                }

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //异常循环调用
                        refreshExchangeRate();
                    }
                }));

    }

    @Override
    public void setAppBackground(boolean appBackground) {
        isAppBackground = appBackground;
    }
}
