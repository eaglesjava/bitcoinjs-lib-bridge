package com.bitbill.www.ui.splash;

import android.os.Bundle;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bitbill.www.app.AppConstants.PLATFORM;

public class SplashActivity extends BaseActivity<SplashMvpPresenter> implements SplashMvpView, GetCacheVersionMvpView, SyncAddressMvpView {

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
}
