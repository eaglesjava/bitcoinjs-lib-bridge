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
    public String getUUIDMD5() {
        return mPrefs.getString(UUID_MD5, null);
    }

    @Override
    public void setUUIDMD5(String uuidmd5) {
        mPrefs.edit().putString(UUID_MD5, uuidmd5).apply();
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
        return mPrefs.getString(FORCE_VERSION, "0.0.1");
    }

    @Override
    public void setForceVersion(String aforceVersion) {
        mPrefs.edit().putString(FORCE_VERSION, aforceVersion).apply();
    }

    @Override
    public String getUpdateVersion() {
        return mPrefs.getString(UPDATE_VERSION, "0.0.1");
    }

    @Override
    public void setUpdateVersion(String aversion) {
        mPrefs.edit().putString(UPDATE_VERSION, aversion).apply();
    }

    @Override
    public double getBtcCnyValue() {
        try {
            return Double.parseDouble(mPrefs.getString(BTC_CNY_VALUE, "0.00"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    @Override
    public void setBtcCnyValue(double btcCnyValue) {
        mPrefs.edit().putString(BTC_CNY_VALUE, String.valueOf(btcCnyValue)).apply();
    }

    @Override
    public double getBtcUsdValue() {

        try {
            return Double.parseDouble(mPrefs.getString(BTC_USD_VALUE, "0.00"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    @Override
    public void setBtcUsdValue(double btcUsdValue) {

        mPrefs.edit().putString(BTC_USD_VALUE, String.valueOf(btcUsdValue)).apply();
    }

    @Override
    public String getApkUrl() {
        return mPrefs.getString(APK_URL, "");
    }

    @Override
    public void setApkUrl(String apkUrl) {
        mPrefs.edit().putString(APK_URL, apkUrl).apply();
    }
}
