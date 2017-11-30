/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.module;

import android.app.Application;
import android.content.Context;

import com.bitbill.www.BuildConfig;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.socket.SocketHelper;
import com.bitbill.www.di.qualifier.ApiInfo;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.di.qualifier.PrefersAppInfo;
import com.bitbill.www.di.qualifier.PrefersWalletInfo;
import com.bitbill.www.di.qualifier.SocketUrlInfo;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.AppModelManager;
import com.bitbill.www.model.app.network.AppApi;
import com.bitbill.www.model.app.network.AppApiHelper;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.app.prefs.AppPreferencesHelper;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.WalletModelManager;
import com.bitbill.www.model.wallet.db.WalletDb;
import com.bitbill.www.model.wallet.db.WalletDbHelper;
import com.bitbill.www.model.wallet.network.WalletApi;
import com.bitbill.www.model.wallet.network.WalletApiHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.socket.client.Socket;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @BaseUrlInfo
    String provideBaseUrl() {
        return BuildConfig.BASE_URL;
    }

    @Provides
    @SocketUrlInfo
    String provideSocketUrl() {
        return BuildConfig.SOCKET_URL;
    }

    @Provides
    @PrefersWalletInfo
    String provideWalletPreferenceName() {
        return AppConstants.PREF_WALLET_NAME;
    }

    @Provides
    @PrefersAppInfo
    String provideAppPreferenceName() {
        return AppConstants.PREF_APP_NAME;
    }

    @Provides
    @Singleton
    AppModel provideAppModuleManager(AppModelManager appModelManager) {
        return appModelManager;
    }

    @Provides
    @Singleton
    AppPreferences provideAppPreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    AppApi provideAppApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey) {
        // TODO: 2017/11/17 生成保护API头部
        return new ApiHeader.ProtectedApiHeader(
                apiKey, null, null);
    }

    @Provides
    @Singleton
    Socket provideSocketIo(SocketHelper socketHelper) {
        return socketHelper.getSocket();
    }

    @Provides
    @Singleton
    WalletModel provideWalletModuleManager(WalletModelManager walletModelManager) {
        return walletModelManager;
    }

    @Provides
    @Singleton
    WalletDb provideWalletDbHelper(WalletDbHelper walletDbHelper) {
        return walletDbHelper;
    }

    @Provides
    @Singleton
    WalletApi provideWalletApiHelper(WalletApiHelper walletApiHelper) {
        return walletApiHelper;
    }


}
