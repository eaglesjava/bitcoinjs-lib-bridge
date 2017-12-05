package com.bitbill.www.app;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.bitbill.www.BuildConfig;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.component.ApplicationComponent;
import com.bitbill.www.di.component.DaggerApplicationComponent;
import com.bitbill.www.di.module.ApplicationModule;

import javax.inject.Inject;

import io.socket.client.Socket;
import okhttp3.OkHttpClient;

/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitbillApp extends Application {
    private static BitbillApp sInstance = new BitbillApp();
    @Inject
    Socket mSocket;
    @Inject
    OkHttpClient mOkhttpClient;
    private ApplicationComponent mApplicationComponent;

    public static BitbillApp get() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        BitcoinJsWrapper.getInstance().init(this);

        AndroidNetworking.initialize(getApplicationContext(), mOkhttpClient);
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        }
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public Socket getSocket() {
        return mSocket;
    }
}

