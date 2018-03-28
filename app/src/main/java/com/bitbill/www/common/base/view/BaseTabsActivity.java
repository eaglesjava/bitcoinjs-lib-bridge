package com.bitbill.www.common.base.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.model.entity.TabItem;
import com.bitbill.www.common.presenter.TabsMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.dialog.CoinSortDialog;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class BaseTabsActivity extends BaseToolbarActivity implements TabsMvpView {

    public static final int TAB_SIZE_LARGE = 3;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.iv_tab_left_arrow)
    ImageView mIvTabLeftArrow;
    @BindView(R.id.iv_tab_right_arrow)
    ImageView mIvTabRightArrow;
    @BindView(R.id.iv_tab_menu)
    ImageView mIvTabMenu;
    private FragmentAdapter mFragmentAdapter;
    private List<TabItem> mTabItems;

    @Override
    public void initView() {

        // TODO: 2018/3/27 for test
        mTabItems = new ArrayList<>();
        mTabItems.add(new TabItem(AppConstants.BTC_COIN_TYPE, "btc", R.drawable.ic_coin_btc, null));
        mTabItems.add(new TabItem("ETH", "eth", R.drawable.ic_coin_eth, null));
        mTabItems.add(new TabItem("EOS", "eos", R.drawable.ic_coin_eos, null));
        loadTabsSuccess(mTabItems);
    }

    @Override
    public void loadTabsSuccess(List<TabItem> tabItems) {
        if (StringUtils.isEmpty(tabItems)) {
            return;
        }
        if (tabItems.size() < TAB_SIZE_LARGE) {
            //隐藏左右滑动按钮
            mIvTabLeftArrow.setVisibility(View.GONE);
            mIvTabRightArrow.setVisibility(View.GONE);
        }
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i < tabItems.size(); i++) {

            TabItem tabItem = tabItems.get(i);
            View customView = getCustomView(tabItem);
            if (i > 0) {
                customView.setAlpha(0.3f);
            }
            tabs.addTab(tabs.newTab().setCustomView(customView));
            if (AppConstants.BTC_COIN_TYPE.equals(tabItem.getCoinId())) {
                mFragmentAdapter.addItem(tabItem.getSymbol(), getBtcFragment());
            } else {
                mFragmentAdapter.addItem(tabItem.getSymbol(), EthInfoFragment.newInstance(tabItem));
            }
        }
        mViewPager.setAdapter(mFragmentAdapter);
        //Tablayout自定义view绑定ViewPager
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                tab.getCustomView().setAlpha(1.0f);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                tab.getCustomView().setAlpha(0.3f);
            }
        });
    }

    private View getCustomView(TabItem tabItem) {
        View newtab = LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        TextView tv = newtab.findViewById(R.id.tv_text);
        tv.setText(tabItem.getSymbol());
        ImageView im = newtab.findViewById(R.id.iv_tab);
        im.setImageResource(tabItem.getIconId());
        return newtab;
    }

    protected abstract BaseFragment getBtcFragment();


    @Override
    public int getLayoutId() {
        return R.layout.activity_base_tabs;
    }


    @OnClick({R.id.iv_tab_left_arrow, R.id.iv_tab_right_arrow, R.id.iv_tab_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tab_left_arrow:
                tabs.smoothScrollBy(-100, 0);
                break;
            case R.id.iv_tab_right_arrow:
                tabs.smoothScrollBy(100, 0);
                break;
            case R.id.iv_tab_menu:
                CoinSortDialog.newInstance(mTabItems).show(getSupportFragmentManager());
                break;
        }
    }
}
