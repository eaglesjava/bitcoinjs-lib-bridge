/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.app.network.AppApi;
import com.bitbill.www.model.app.network.entity.FeeBackRequest;
import com.bitbill.www.model.app.network.entity.GetConfigResponse;
import com.bitbill.www.model.app.network.entity.GetExchangeRateResponse;
import com.bitbill.www.model.app.prefs.AppPreferences;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */

@Singleton
public class AppModelManager extends ModelManager implements AppModel {

    private final AppApi mAppApi;
    private final AppPreferences mAppPreferences;
    private final Context mContext;

    @Inject
    public AppModelManager(@ApplicationContext Context context, AppPreferences appPreferences, AppApi appApi) {
        mAppApi = appApi;
        mAppPreferences = appPreferences;
        mContext = context;
    }


    @Override
    public void clear() {
        mAppPreferences.clear();
    }

    @Override
    public ApiHeader getApiHeader() {
        return mAppApi.getApiHeader();
    }

    @Override
    public void setReceiveRemindDialogShown() {
        mAppPreferences.setReceiveRemindDialogShown();
    }

    @Override
    public boolean isReceiveRemindDialogShown() {
        return mAppPreferences.isReceiveRemindDialogShown();
    }

    @Override
    public boolean isShortcutShown() {
        return mAppPreferences.isShortcutShown();
    }

    @Override
    public void setShortcutShown(boolean shown) {
        mAppPreferences.setShortcutShown(shown);
    }

    @Override
    public boolean isSoundEnable() {
        return mAppPreferences.isSoundEnable();
    }

    @Override
    public void setSoundEnabled(boolean soundEnabled) {
        mAppPreferences.setSoundEnabled(soundEnabled);
    }

    @Override
    public SelectedCurrency getSelectedCurrency() {
        return mAppPreferences.getSelectedCurrency();
    }

    @Override
    public void setSelectedCurrency(SelectedCurrency selectedCurrency) {
        mAppPreferences.setSelectedCurrency(selectedCurrency);
    }

    @Override
    public String getContactKey() {
        return mAppPreferences.getContactKey();
    }

    @Override
    public void setContactkey(String contactKey) {
        mAppPreferences.setContactkey(contactKey);
    }

    @Override
    public String getUUIDMD5() {
        return mAppPreferences.getUUIDMD5();
    }

    @Override
    public void setUUIDMD5(String uuidmd5) {
        mAppPreferences.setUUIDMD5(uuidmd5);
    }

    @Override
    public Locale getSelectedLocale() {
        return mAppPreferences.getSelectedLocale();
    }

    @Override
    public void setSelectedLocale(Locale locale) {
        mAppPreferences.setSelectedLocale(locale);
    }

    /**
     * 获取当前的Locale
     *
     * @return Locale
     */
    @Override
    public Locale getCurrentLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            locale = mContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = mContext.getResources().getConfiguration().locale;
        }
        return locale;
    }

    /**
     * 更新Locale
     */
    @Override
    public void updateLocale(Context context) {
        updateLocale(context, null);
    }

    /**
     * 更新Locale
     */
    @Override
    public void updateLocale(Context context, Configuration newConfig) {
        Locale selectedLocale = getSelectedLocale();
        if (selectedLocale == null) {
            return;
        }
        Locale.setDefault(selectedLocale);
        Configuration configuration = newConfig != null ? new Configuration(newConfig) : mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(selectedLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(selectedLocale);
        } else {
            configuration.locale = selectedLocale;
        }
        DisplayMetrics _DisplayMetrics = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(configuration, _DisplayMetrics);
    }

    @Override
    public Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context);
        } else {
            return context;
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale = getSelectedLocale();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    @Override
    public boolean isAliasSeted() {
        return mAppPreferences.isAliasSeted();
    }

    @Override
    public void setAliasSeted(boolean isSeted) {
        mAppPreferences.setAliasSeted(isSeted);
    }

    @Override
    public String getForceVersion() {
        return mAppPreferences.getForceVersion();
    }

    @Override
    public void setForceVersion(String aforceVersion) {
        mAppPreferences.setForceVersion(aforceVersion);
    }

    @Override
    public String getUpdateVersion() {
        return mAppPreferences.getUpdateVersion();
    }

    @Override
    public void setUpdateVersion(String aversion) {
        mAppPreferences.setUpdateVersion(aversion);
    }

    @Override
    public double getBtcCnyValue() {
        return mAppPreferences.getBtcCnyValue();
    }

    @Override
    public void setBtcCnyValue(double btcCnyValue) {
        mAppPreferences.setBtcCnyValue(btcCnyValue);
    }

    @Override
    public double getBtcUsdValue() {
        return mAppPreferences.getBtcUsdValue();
    }

    @Override
    public void setBtcUsdValue(double btcUsdValue) {
        mAppPreferences.setBtcUsdValue(btcUsdValue);
    }

    /**
     * 获取btc兑换比例
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<GetExchangeRateResponse>> getExchangeRate() {
        return mAppApi.getExchangeRate();
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<GetConfigResponse>> getConfig() {
        return mAppApi.getConfig();
    }

    @Override
    public Observable<ApiResponse> feeBack(FeeBackRequest feeBackRequest) {
        return mAppApi.feeBack(feeBackRequest);
    }

    @Override
    public void downloadFile(String url, String dirPath, String fileName, DownloadProgressListener downloadProgressListener, DownloadListener downloadListener) {
        mAppApi.downloadFile(url, dirPath, fileName, downloadProgressListener, downloadListener);
    }
}
