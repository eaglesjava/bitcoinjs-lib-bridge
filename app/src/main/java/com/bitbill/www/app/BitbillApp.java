package com.bitbill.www.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.www.BuildConfig;
import com.bitbill.www.R;
import com.bitbill.www.common.utils.LocaleUtils;
import com.bitbill.www.common.utils.SoundUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.component.ApplicationComponent;
import com.bitbill.www.di.component.DaggerApplicationComponent;
import com.bitbill.www.di.module.ApplicationModule;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.eventbus.UnConfirmEvent;
import com.bitbill.www.model.wallet.db.WalletDbHelper;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.socket.Register;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    @Inject
    WalletDbHelper mWalletDbHelper;
    @Inject
    AppModel mAppModel;
    ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //强制修改应用语言
            LocaleUtils.updateLocale(activity, LocaleUtils.getUserLocale(activity));
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
        //Activity 其它生命周期的回调
    };
    private ApplicationComponent mApplicationComponent;
    private List<Wallet> mWallets;
    private double mBtcCnyValue;
    private double mBtcUsdValue;
    private AppPreferences.SelectedCurrency mSelectedCurrency = AppPreferences.SelectedCurrency.CNY;
    private long mBlockHeight;
    private String mContactKey;

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
        registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale _UserLocale = LocaleUtils.getUserLocale(this);
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale);
            Configuration _Configuration = new Configuration(newConfig);
            _Configuration.setLocale(_UserLocale);
            getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
        }

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
                JSONObject result = null;
                try {
                    result = new JSONObject(String.valueOf(args[0]));
                    Log.d(TAG, "EVENT_CONFIRM called with: args = [" + result + "]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  获取未确认列表
                EventBus.getDefault().postSticky(new UnConfirmEvent());
            }
        }).on(EVENT_UNCONFIRM, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONObject result = null;
                try {
                    result = new JSONObject(String.valueOf(args[0]));
                    Log.d(TAG, "EVENT_UNCONFIRM called with: args = [" + result + "]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // TODO: 2018/1/4 根据设置开启音效
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
        if (defaultWallet == null) {
            defaultWallet = mWallets.get(0);
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

    public double getBtcCnyValue() {
        return mBtcCnyValue;
    }

    public void setBtcCnyValue(double btcCnyValue) {
        mBtcCnyValue = btcCnyValue;
    }

    public double getBtcUsdValue() {
        return mBtcUsdValue;
    }

    public void setBtcUsdValue(double btcUsdValue) {
        mBtcUsdValue = btcUsdValue;
    }

    public String getBtcValue(String btcAmount) {
        if (mSelectedCurrency == null) {
            return "0.00";
        }
        double btcRate;
        if (mSelectedCurrency.equals(AppPreferences.SelectedCurrency.CNY)) {
            btcRate = getBtcCnyValue();
        } else {
            btcRate = getBtcUsdValue();
        }
        if (btcRate == 0) {
            return "0.00";
        }
        return String.format(getString(R.string.text_btc_cny_value), StringUtils.multiplyValue(btcRate, btcAmount), mSelectedCurrency.name());
    }

    public void setSelectedCurrency(AppPreferences.SelectedCurrency selectedCurrency) {
        mSelectedCurrency = selectedCurrency;
    }

    public long getBlockHeight() {
        return mBlockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        mBlockHeight = blockHeight;
    }

    public DaoSession getDaoSession() {
        return mWalletDbHelper.getDaoSession();
    }

    public String getContactKey() {
        return mContactKey;
    }

    public void setContactKey(String contactKey) {
        mContactKey = contactKey;
    }
}

