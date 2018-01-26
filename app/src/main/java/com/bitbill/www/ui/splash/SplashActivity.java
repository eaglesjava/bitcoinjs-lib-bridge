package com.bitbill.www.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.presenter.GetCacheVersionMvpPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpView;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.eventbus.RegisterEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.socket.Register;
import com.bitbill.www.service.SocketServiceProvider;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.bitbill.www.app.AppConstants.PLATFORM;

public class SplashActivity extends BaseActivity<SplashMvpPresenter> implements SplashMvpView, GetCacheVersionMvpView, SyncAddressMvpView {

    private static final String TAG = "SplashActivity";
    private static final int MSG_SET_ALIAS = 1001;
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success:" + alias;
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.:" + alias;
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code + ",alias:" + alias;
                    Log.e(TAG, logs);
            }
        }
    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
    @BindView(R.id.fl_content)
    View flContent;
    @Inject
    SplashMvpPresenter<WalletModel, SplashMvpView> mSplashMvpPresenter;
    @Inject
    GetCacheVersionMvpPresenter<WalletModel, GetCacheVersionMvpView> mGetCacheVersionMvpPresenter;
    @Inject
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> mSyncAddressMvpPresentder;
    private boolean mHasWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getMvpPresenter().hasWallet();
        getMvpPresenter().getExchangeRate();
        getApp().setContactKey(getMvpPresenter().getContactKey());

        startSocketService();

        setAlias();

    }

    private void startSocketService() {
        SocketServiceProvider.start(SplashActivity.this);
    }

    @Override
    public SplashMvpPresenter getMvpPresenter() {
        return mSplashMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
        addPresenter(mGetCacheVersionMvpPresenter);
    }

    @Override
    public void hasWallet(List<Wallet> walletList) {
        if (mHasWallet = !StringUtils.isEmpty(walletList)) {
            for (Wallet wallet : walletList) {
                //注册钱包
                EventBus.getDefault().post(new RegisterEvent().setData(new Register(wallet.getName(), "", DeviceUtil.getDeviceId(), PLATFORM)));
            }
        }
        mGetCacheVersionMvpPresenter.getCacheVersion();
        flContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpNext();
            }
        }, 2000);

    }

    private void jumpNext() {
        if (mHasWallet) {
            //本地有钱包直接跳转到首页
            MainActivity.start(SplashActivity.this);
        } else {
            GuideActivity.start(SplashActivity.this);
        }
        finish();
    }

    @Override
    public void getResponseAddressIndex(long indexNo, long changeIndexNo, Wallet wallet) {
        mSyncAddressMvpPresentder.syncLastAddressIndex(indexNo, changeIndexNo, wallet);
    }

    @Override
    public void getDiffVersionWallets(List<Wallet> tmpWalletList) {

    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, DeviceUtil.getDeviceId()));
    }
}
