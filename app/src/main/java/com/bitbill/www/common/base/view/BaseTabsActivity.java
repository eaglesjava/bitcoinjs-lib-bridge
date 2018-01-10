package com.bitbill.www.common.base.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import butterknife.BindView;

public abstract class BaseTabsActivity extends BaseToolbarActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private FragmentAdapter mFragmentAdapter;

    @Override
    public void initView() {
        setUpViewPager();
    }

    /**
     * Set up the ViewPager with the sections adapter.
     */
    private void setUpViewPager() {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.addItem("btc", getBtcFragment());
        mFragmentAdapter.addItem("bch", BchInfoFragment.newInstance());
        mFragmentAdapter.addItem("eth", EthInfoFragment.newInstance());
        mViewPager.setAdapter(mFragmentAdapter);
        tabs.setupWithViewPager(mViewPager);
        //禁止tab选择
        LinearLayout tabStrip = (LinearLayout) tabs.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null) {
                tabView.setClickable(false);
            }
        }
        tabs.setSelectedTabIndicatorColor(getResources().getColor(isBlue() ? R.color.blue : R.color.black));
    }

    protected abstract boolean isBlue();

    protected abstract BaseFragment getBtcFragment();


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_info;
    }

}
