/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.module;

import android.app.Application;
import android.content.Context;

import com.bitbill.model.db.dao.DaoMaster;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.www.BuildConfig;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.db.DbOpenHelper;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.socket.SocketHelper;
import com.bitbill.www.di.qualifier.ApiInfo;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.di.qualifier.PrefersAppInfo;
import com.bitbill.www.di.qualifier.PrefersWalletInfo;
import com.bitbill.www.di.qualifier.SocketUrlInfo;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.AddressModelManager;
import com.bitbill.www.model.address.db.AddressDb;
import com.bitbill.www.model.address.db.AddressDbHelper;
import com.bitbill.www.model.address.network.AddressApi;
import com.bitbill.www.model.address.network.AddressApiHelper;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.AppModelManager;
import com.bitbill.www.model.app.network.AppApi;
import com.bitbill.www.model.app.network.AppApiHelper;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.app.prefs.AppPreferencesHelper;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.ContactModelManager;
import com.bitbill.www.model.contact.db.ContactDb;
import com.bitbill.www.model.contact.db.ContactDbHelper;
import com.bitbill.www.model.contact.network.ContactApi;
import com.bitbill.www.model.contact.network.ContactApiHelper;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.TxModelManager;
import com.bitbill.www.model.transaction.db.TxDb;
import com.bitbill.www.model.transaction.db.TxDbHelper;
import com.bitbill.www.model.transaction.network.TxApi;
import com.bitbill.www.model.transaction.network.TxApiHelper;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.WalletModelManager;
import com.bitbill.www.model.wallet.db.WalletDb;
import com.bitbill.www.model.wallet.db.WalletDbHelper;
import com.bitbill.www.model.wallet.network.WalletApi;
import com.bitbill.www.model.wallet.network.WalletApiHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

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
    @DatabaseInfo
    DaoSession provideDaoSession(DbOpenHelper dbOpenHelper) {
        //release 使用加密数据库
        return new DaoMaster(BuildConfig.DEBUG
                ? dbOpenHelper.getWritableDb()
                : dbOpenHelper.getEncryptedWritableDb(BuildConfig.ENCRYPTED_DB_SECRECT))
                .newSession();
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
    OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        return builder.build();
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

    @Provides
    @Singleton
    ContactModel provideContactModuleManager(ContactModelManager contactModelManager) {
        return contactModelManager;
    }

    @Provides
    @Singleton
    ContactDb provideContactDbHelper(ContactDbHelper contactDbHelper) {
        return contactDbHelper;
    }

    @Provides
    @Singleton
    ContactApi provideContactApiHelper(ContactApiHelper contactApiHelper) {
        return contactApiHelper;
    }

    @Provides
    @Singleton
    AddressModel provideAddressModuleManager(AddressModelManager addressModelManager) {
        return addressModelManager;
    }

    @Provides
    @Singleton
    AddressDb provideAddressDbHelper(AddressDbHelper addressDbHelper) {
        return addressDbHelper;
    }

    @Provides
    @Singleton
    AddressApi provideAddressApiHelper(AddressApiHelper addressApiHelper) {
        return addressApiHelper;
    }

    @Provides
    @Singleton
    TxModel provideTxModuleManager(TxModelManager txModelManager) {
        return txModelManager;
    }

    @Provides
    @Singleton
    TxDb provideTxDbHelper(TxDbHelper txDbHelper) {
        return txDbHelper;
    }

    @Provides
    @Singleton
    TxApi provideTxApiHelper(TxApiHelper txApiHelper) {
        return txApiHelper;
    }

}
