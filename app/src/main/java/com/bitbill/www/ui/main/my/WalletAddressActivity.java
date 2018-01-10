package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseTabsActivity;
import com.bitbill.www.model.wallet.db.entity.Wallet;

public class WalletAddressActivity extends BaseTabsActivity {
    private Wallet mWallet;

    public static void start(Context context, Wallet wallet) {
        Intent starter = new Intent(context, WalletAddressActivity.class);
        starter.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean isBlue() {
        return false;
    }

    @Override
    protected BaseFragment getBtcFragment() {
        return WalletAddressFragment.newInstance(mWallet);
    }
}
