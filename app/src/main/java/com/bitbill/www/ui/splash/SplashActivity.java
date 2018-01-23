package com.bitbill.www.ui.splash;

import android.os.Bundle;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.presenter.GetCacheVersionMvpPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpView;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        // 根据是否是第一次进入判断跳转到引导页还是首页
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
    public void hasWallet(boolean hasWallet) {
        mHasWallet = hasWallet;
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
