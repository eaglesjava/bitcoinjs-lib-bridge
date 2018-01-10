package com.bitbill.www.ui.wallet.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.info.transfer.TransferDetailsActivity;

import butterknife.BindView;

public class WalletInfoActivity extends BaseToolbarActivity implements BtcRecordFragment.OnTransactionRecordItemClickListener {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private FragmentAdapter mFragmentAdapter;
    private Wallet mWallet;

    public static void start(Context context, Wallet wallet) {
        Intent intent = new Intent(context, WalletInfoActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
    }

    @Override
    public void OnTransactionRecordItemClick(TxRecord item) {
        //跳转到转账详情页面
        TransferDetailsActivity.start(WalletInfoActivity.this, item);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) intent.getSerializableExtra(AppConstants.EXTRA_WALLET);
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
    public void initView() {
        setUpViewPager();
    }

    /**
     * Set up the ViewPager with the sections adapter.
     */
    private void setUpViewPager() {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.addItem("btc", BtcRecordFragment.newInstance(mWallet));
        mFragmentAdapter.addItem("bch", BchInfoFragment.newInstance());
        mFragmentAdapter.addItem("eth", EthInfoFragment.newInstance());
        mViewPager.setAdapter(mFragmentAdapter);
        tabs.setupWithViewPager(mViewPager);  //禁止tab选择
        LinearLayout tabStrip = (LinearLayout) tabs.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null) {
                tabView.setClickable(false);
            }
        }
    }


    @Override
    public void initData() {
        setTitle(mWallet.getName() + " 的钱包");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_info;
    }

}
