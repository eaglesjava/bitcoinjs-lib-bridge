package com.bitbill.www.ui.splash;

import android.os.Bundle;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashMvpPresenter> implements SplashMvpView {

    @BindView(R.id.fl_content)
    View flContent;
    @Inject
    SplashMvpPresenter<WalletModel, SplashMvpView> mSplashMvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        flContent.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 根据是否是第一次进入判断跳转到引导页还是首页
                getMvpPresenter().hasWallet();
            }
        }, 2000);
        getMvpPresenter().getExchangeRate();
    }

    @Override
    public SplashMvpPresenter getMvpPresenter() {
        return mSplashMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void hasWallet(boolean hasWallet) {
        if (hasWallet) {
            //本地有钱包直接跳转到首页
            MainActivity.start(SplashActivity.this);
        } else {
            GuideActivity.start(SplashActivity.this);
        }
        finish();
    }
}
