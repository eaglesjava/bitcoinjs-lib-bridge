package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2018/1/4.
 */
@PerActivity
public class SystemSettingPresenter<M extends AppModel, V extends SystemSettingMvpView> extends ModelPresenter<M, V> implements SystemSettingMvpPresenter<M, V> {

    @Inject
    public SystemSettingPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public boolean isSoundEnable() {
        return getModelManager().isSoundEnable();
    }

    @Override
    public void setSoundEnabled(boolean soundEnabled) {
        getModelManager().setSoundEnabled(soundEnabled);
    }

    @Override
    public AppPreferences.SelectedCurrency getSelectedCurrency() {
        return getModelManager().getSelectedCurrency();
    }

    @Override
    public void setSelectedCurrency(AppPreferences.SelectedCurrency selectedCurrency) {
        getModelManager().setSelectedCurrency(selectedCurrency);
        getApp().setSelectedCurrency(selectedCurrency);
    }

    @Override
    public Locale getSelectedLocale() {
        return getModelManager().getSelectedLocale();
    }

    @Override
    public void setSelectedLocale(Locale locale) {
        getModelManager().setSelectedLocale(locale);
    }

    /**
     * 判断需不需要更新
     *
     * @param newUserLocale New User Locale
     * @return true / false
     */
    @Override
    public boolean needUpdateLocale(Locale newUserLocale) {
        return newUserLocale != null && !newUserLocale.equals(getSelectedLocale());
    }
}
