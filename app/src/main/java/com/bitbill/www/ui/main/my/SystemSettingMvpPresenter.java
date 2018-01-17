package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;

import java.util.Locale;

/**
 * Created by isanwenyu on 2018/1/4.
 */

public interface SystemSettingMvpPresenter<M extends AppModel, V extends SystemSettingMvpView> extends MvpPresenter<V> {

    boolean isSoundEnable();

    void setSoundEnabled(boolean soundEnabled);

    AppPreferences.SelectedCurrency getSelectedCurrency();

    void setSelectedCurrency(AppPreferences.SelectedCurrency selectedCurrency);

    Locale getSelectedLocale();

    void setSelectedLocale(Locale locale);

    void updateLocale(Locale newUserLocale);
}
