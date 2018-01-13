package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.network.entity.GetExchangeRateResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/1.
 */
@PerActivity
public class SplashPresenter<M extends WalletModel, V extends SplashMvpView> extends ModelPresenter<M, V> implements SplashMvpPresenter<M, V> {
    @Inject
    public SplashPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void hasWallet() {
        getCompositeDisposable().add(getModelManager().hasWallet()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hasWallet(aBoolean);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hasWallet(false);
                    }
                }));
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

}
