package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import butterknife.BindView;

public class AddContactByAddressActivity extends BaseToolbarActivity {


    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private FragmentAdapter mFragmentAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AddContactByAddressActivity.class);
        context.startActivity(starter);
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
        mFragmentAdapter.addItem("btc", AddBtcContactByAddressFragment.newInstance());
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

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_contact_by_address;
    }
}
