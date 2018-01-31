/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.prefs;

import android.content.Context;

import com.bitbill.www.common.base.model.prefs.PreferencesHelper;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.di.qualifier.PrefersAppInfo;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */

@Singleton
public class AppPreferencesHelper extends PreferencesHelper implements AppPreferences {


    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context, @PrefersAppInfo String prefFileName) {
        super(context, prefFileName);
    }

    @Override
    public void setReceiveRemindDialogShown() {

        mPrefs.edit().putBoolean(IS_RECEIVE_REMIND_DIALOG_SHOWN, true).apply();
    }

    @Override
    public boolean isReceiveRemindDialogShown() {

        return mPrefs.getBoolean(IS_RECEIVE_REMIND_DIALOG_SHOWN, false);
    }

    @Override
    public boolean isShortcutShown() {
        return mPrefs.getBoolean(IS_SHORT_CUT_SHOWN, true);
    }

    @Override
    public void setShortcutShown(boolean shown) {

        mPrefs.edit().putBoolean(IS_SHORT_CUT_SHOWN, shown).apply();
    }

    @Override
    public boolean isSoundEnable() {
        return mPrefs.getBoolean(IS_SOUND_ENABLED, true);
    }

    @Override
    public void setSoundEnabled(boolean soundEnabled) {

        mPrefs.edit().putBoolean(IS_SOUND_ENABLED, soundEnabled).apply();

    }

    @Override
    public SelectedCurrency getSelectedCurrency() {
        return SelectedCurrency.valueOf(mPrefs.getString(SELECTED_CURRENCY, SelectedCurrency.CNY.name()));

    }

    @Override
    public void setSelectedCurrency(SelectedCurrency selectedCurrency) {
        mPrefs.edit().putString(SELECTED_CURRENCY, selectedCurrency.name()).apply();

    }

    @Override
    public String getContactKey() {
        return mPrefs.getString(CONTACTKEY, null);
    }

    @Override
    public void setContactkey(String contactKey) {
        mPrefs.edit().putString(CONTACTKEY, contactKey).apply();
    }

    @Override
    public Locale getSelectedLocale() {

        String language = mPrefs.getString(SELECTED_LOCALE, null);
        if (StringUtils.isNotEmpty(language)) {
            return new Locale(language);
        }
        return null;

    }

    @Override
    public void setSelectedLocale(Locale locale) {
        if (locale == null) {
            return;
        }
        mPrefs.edit().putString(SELECTED_LOCALE, locale.getLanguage()).apply();
    }

    @Override
    public boolean isAliasSeted() {
        return mPrefs.getBoolean(IS_ALIAS_SETED, false);
    }

    @Override
    public void setAliasSeted(boolean isSeted) {
        mPrefs.edit().putBoolean(IS_ALIAS_SETED, isSeted).apply();
    }

    @Override
    public String getForceVersion() {
        return mPrefs.getString(FORCE_VERSION, "0.0");
    }

    @Override
    public void setForceVersion(String aforceVersion) {
        mPrefs.edit().putString(FORCE_VERSION, aforceVersion).apply();
    }

    @Override
    public String getUpdateVersion() {
        return mPrefs.getString(UPDATE_VERSION, "0.0");
    }

    @Override
    public void setUpdateVersion(String aversion) {
        mPrefs.edit().putString(UPDATE_VERSION, aversion).apply();
    }
}
