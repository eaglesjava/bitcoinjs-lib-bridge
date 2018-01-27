package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetExchangeRateResponse;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/1.
 */
@PerActivity
public class SplashPresenter<M extends AppModel, V extends SplashMvpView> extends ModelPresenter<M, V> implements SplashMvpPresenter<M, V> {
    @Inject
    WalletModel mWalletModel;

    @Inject
    public SplashPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void hasWallet() {
        getCompositeDisposable().add(mWalletModel.getAllWallets()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<Wallet>>() {
                    @Override
                    public void onNext(List<Wallet> walletList) {
                        super.onNext(walletList);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hasWallet(walletList);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hasWallet(null);
                    }
                }));
    }

    @Override
    public void getExchangeRate() {
        getCompositeDisposable().add(mWalletModel.getExchangeRate()
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

    @Override
    public String getContactKey() {

        String contactKey = getModelManager().getContactKey();
        if (StringUtils.isEmpty(contactKey)) {
            contactKey = StringUtils.getContactKey();
            getModelManager().setContactkey(contactKey);
        }
        return contactKey;
    }

    @Override
    public boolean isAliasSeted() {
        return getModelManager().isAliasSeted();
    }

    @Override
    public void setAliasSeted(boolean isSeted) {
        getModelManager().setAliasSeted(isSeted);
    }

    @Override
    public void initLanguage() {
        if (getModelManager().getSelectedLocale() == null) {
            //当前没有设置语言并且当前币种没有设置的情况下
            if (getModelManager().getSelectedCurrency() == null) {
                getModelManager().setSelectedCurrency(Locale.CHINESE.equals(getModelManager().getCurrentLocale()) ? AppPreferences.SelectedCurrency.CNY : AppPreferences.SelectedCurrency.USD);
            }
            getModelManager().setSelectedLocale(getModelManager().getCurrentLocale());
        }
    }
}
