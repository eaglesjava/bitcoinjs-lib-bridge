package com.bitbill.www.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.www.BuildConfig;
import com.bitbill.www.R;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.component.ApplicationComponent;
import com.bitbill.www.di.component.DaggerApplicationComponent;
import com.bitbill.www.di.module.ApplicationModule;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.model.wallet.db.WalletDbHelper;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitbillApp extends Application {
    private static final String TAG = "BitbillApp";
    private static BitbillApp sInstance;
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
            mAppModel.updateLocale(activity);
            mAppModel.updateLocale(getApplicationContext());
        }

        @Override
        public void onActivityStarted(Activity activity) {
//            if (isBackGround) {
//                isBackGround = false;
//                //通知SocketServiceProvider
//                EventBus.getDefault().post(new AppBackgroundEvent(isBackGround));
//            }
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
    private AppPreferences.SelectedCurrency mSelectedCurrency;
    private long mBlockHeight;
    private String mContactKey;
    private boolean isBackGround;

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

        registerActivityLifecycleCallbacks(callbacks);
        mAppModel.updateLocale(this);

        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //系统语言改变了应用保持之前设置的语言
        mAppModel.updateLocale(this, newConfig);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
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

    public boolean hasBtcRate() {
        return getBtcCnyValue() > 0 && getBtcUsdValue() > 0;
    }

    public double getBtcCnyValue() {
        return mAppModel.getBtcCnyValue();
    }

    public void setBtcCnyValue(double btcCnyValue) {
        mAppModel.setBtcCnyValue(btcCnyValue);
    }

    public double getBtcUsdValue() {
        return mAppModel.getBtcUsdValue();
    }

    public void setBtcUsdValue(double btcUsdValue) {
        mAppModel.setBtcUsdValue(btcUsdValue);
    }

    public String getBtcValue(String btcAmount) {

        if (mSelectedCurrency == null) {
            mSelectedCurrency = mAppModel.getSelectedCurrency();
        }
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

    public String getUUIDMD5() {

        String uuidmd5 = mAppModel.getUUIDMD5();
        if (StringUtils.isEmpty(uuidmd5)) {
            uuidmd5 = StringUtils.getUUIDMD5();
            mAppModel.setUUIDMD5(uuidmd5);
        }
        return uuidmd5;
    }

    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    Log.d(TAG, "isRunningForeGround");
                    return true;
                }
            }
        }
        Log.d(TAG, "isRunningBackGround");
        return false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//        if (level == TRIM_MEMORY_UI_HIDDEN) {
//            isBackGround = true;
//            //通知SocketServiceProvider
//            EventBus.getDefault().post(new AppBackgroundEvent(isBackGround));
//            Log.d(TAG, "APP遁入后台");
//        }
    }

}

