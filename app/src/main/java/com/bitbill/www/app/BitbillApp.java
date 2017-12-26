package com.bitbill.www.app;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.bitbill.www.BuildConfig;
import com.bitbill.www.R;
import com.bitbill.www.common.utils.SoundUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.component.ApplicationComponent;
import com.bitbill.www.di.component.DaggerApplicationComponent;
import com.bitbill.www.di.module.ApplicationModule;
import com.bitbill.www.model.eventbus.UnConfirmEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.socket.Register;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

import static com.bitbill.www.app.AppConstants.EVENT_CONFIRM;
import static com.bitbill.www.app.AppConstants.EVENT_REGISTER;
import static com.bitbill.www.app.AppConstants.EVENT_UNCONFIRM;

/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitbillApp extends Application {
    private static final String TAG = "BitbillApp";
    private static BitbillApp sInstance;
    @Inject
    Socket mSocket;
    @Inject
    OkHttpClient mOkhttpClient;
    private ApplicationComponent mApplicationComponent;
    private List<Wallet> mWallets;
    private double mBtcValue = 100000;// TODO: 2017/12/25 for test

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
        mWallets = new ArrayList<>();

        initSocket();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    private void initSocket() {

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT called with: args = [" + args + "]");
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                Log.d(TAG, "EVENT_DISCONNECT called with: args = [" + args + "]");
            }

        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT_TIMEOUT called with: args = [" + args + "]");
            }
        }).on(EVENT_REGISTER, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_REGISTER called with: args = [" + args + "]");
            }
        }).on(EVENT_CONFIRM, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONFIRM called with: args = [" + args + "]");
                //  获取未确认列表
                EventBus.getDefault().postSticky(new UnConfirmEvent());
            }
        }).on(EVENT_UNCONFIRM, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d(TAG, "EVENT_UNCONFIRM called with: args = [" + args + "]");
                // 播放声音
                SoundUtils.playSound(R.raw.diaoluo_da);
                //  获取未确认列表
                EventBus.getDefault().postSticky(new UnConfirmEvent());
            }
        });
        mSocket.connect();
    }

    public Socket getSocket() {
        return mSocket;
    }

    public List<Wallet> getWallets() {
        return mWallets;
    }

    public void setWallets(List<Wallet> wallets) {
        mWallets = wallets;
    }


    @Nullable
    public Wallet getDefaultWallet() {
        if (StringUtils.isEmpty(mWallets)) return null;
        Wallet defaultWallet = null;
        for (Wallet wallet : mWallets) {
            if (wallet.getIsDefault()) {
                defaultWallet = wallet;
                return defaultWallet;
            }
        }
        return defaultWallet;
    }


    public void registerWallet(Register register) {
        mSocket.emit(EVENT_REGISTER, register.jsonString());
        Log.d(TAG, "registerWallet() called with: register = [" + register.jsonString() + "]");
    }

    public boolean getSocketConnected() {
        if (mSocket == null) {
            return false;
        }
        return mSocket.connected();
    }

    public double getBtcValue() {
        return mBtcValue;
    }

    public void setBtcValue(double btcValue) {
        mBtcValue = btcValue;
    }
}

