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
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.AppModelManager;
import com.bitbill.www.model.app.network.AppApi;
import com.bitbill.www.model.app.network.AppApiHelper;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.app.prefs.AppPreferencesHelper;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.btc.BtcModelManager;
import com.bitbill.www.model.btc.db.BtcDb;
import com.bitbill.www.model.btc.db.BtcDbHelper;
import com.bitbill.www.model.btc.network.BtcApi;
import com.bitbill.www.model.btc.network.BtcApiHelper;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.ContactModelManager;
import com.bitbill.www.model.contact.db.ContactDb;
import com.bitbill.www.model.contact.db.ContactDbHelper;
import com.bitbill.www.model.contact.network.ContactApi;
import com.bitbill.www.model.contact.network.ContactApiHelper;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.WalletModelManager;
import com.bitbill.www.model.wallet.db.WalletDb;
import com.bitbill.www.model.wallet.db.WalletDbHelper;
import com.bitbill.www.model.wallet.network.WalletApi;
import com.bitbill.www.model.wallet.network.WalletApiHelper;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

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
    OkHttpClient provideOkhttpClient(@ApplicationContext Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        InputStream cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = context.getAssets().open("ca/b4d4e57017f4e840.crt");
            Certificate ca;
            ca = cf.generateCertificate(cert);

            // creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // creating a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // 获取默认的 HostnameVerifier
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    // 如果直接返回 true，等于不做域名校验
                    return hv.verify(AppConstants.HOST_BITBILL_COM, session);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                cert.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    BtcModel provideBtcModelManager(BtcModelManager btcModelManager) {
        return btcModelManager;
    }

    @Provides
    @Singleton
    BtcDb provideBtcDbHelper(BtcDbHelper btcDbHelper) {
        return btcDbHelper;
    }

    @Provides
    @Singleton
    BtcApi provideBtcApiHelper(BtcApiHelper btcApiHelper) {
        return btcApiHelper;
    }

}
