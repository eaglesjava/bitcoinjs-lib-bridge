package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

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
    public void setContactKey() {

        String contactKey = getModelManager().getContactKey();
        if (StringUtils.isEmpty(contactKey)) {
            contactKey = StringUtils.getContactKey();
            getModelManager().setContactkey(contactKey);
        }
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
            //选取当前的语言设置币种
            AppPreferences.SelectedCurrency selectedCurrency = StringUtils.equals(Locale.SIMPLIFIED_CHINESE, getModelManager().getCurrentLocale()) ? AppPreferences.SelectedCurrency.CNY : AppPreferences.SelectedCurrency.USD;
            getModelManager().setSelectedCurrency(selectedCurrency);
            getApp().setSelectedCurrency(selectedCurrency);
            getModelManager().setSelectedLocale(getModelManager().getCurrentLocale());
        }
    }
}
