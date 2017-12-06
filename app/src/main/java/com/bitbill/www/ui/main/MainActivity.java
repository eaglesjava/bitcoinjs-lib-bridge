package com.bitbill.www.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bitbill.www.R;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.entity.eventbus.BackupSuccessEvent;
import com.bitbill.www.model.entity.eventbus.CreateSuccessEvent;
import com.bitbill.www.model.wallet.network.entity.TransactionRecord;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.wallet.info.BtcRecordFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainMvpPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainMvpView, BtcRecordFragment.OnTransactionRecordItemClickListener {

    @Inject
    MainMvpPresenter<AppModel, MainMvpView> mMainMvpPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private AssetFragment mAssetFragment;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public MainMvpPresenter getMvpPresenter() {
        return mMainMvpPresenter;
    }

    @Override
    public void injectComponent() {
        //inject activity
        getActivityComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAssetFragment = AssetFragment.newInstance();
        mAdapter.addItem(mAssetFragment);
        mAdapter.addItem(ReceiveFragment.newInstance());
        mAdapter.addItem(SendFragment.newInstance());
        mAdapter.addItem(MyFragment.newInstance());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_asset) {
            // 切换到资产界面
            mViewPager.setCurrentItem(0, false);
            setTitle(R.string.title_asset);

        } else if (id == R.id.nav_receive) {
            // 切换到接收界面
            mViewPager.setCurrentItem(1, false);
            setTitle(R.string.title_receive);

        } else if (id == R.id.nav_send) {
            // 切换到发送界面
            mViewPager.setCurrentItem(2, false);
            setTitle(R.string.title_send);

        } else if (id == R.id.nav_contact) {
            // 切换到联系人界面
            mViewPager.setCurrentItem(3, false);
            setTitle(R.string.title_contact);
        }
        // TODO: 2017/12/6 for test
        else if (id == R.id.nav_guide) {
            GuideActivity.start(MainActivity.this);

        }
        // TODO: 2017/11/17 add other nav item

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnTransactionRecordItemClick(TransactionRecord item) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletBackupSuccess(BackupSuccessEvent walletBackupSuccess) {
        BackupSuccessEvent stickyEvent = EventBus.getDefault().removeStickyEvent(BackupSuccessEvent.class);
        //重新加载钱包信息
        if (mAssetFragment != null) {
            mAssetFragment.initData();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletBackupSuccess(CreateSuccessEvent createSuccessEvent) {
        CreateSuccessEvent stickyEvent = EventBus.getDefault().removeStickyEvent(CreateSuccessEvent.class);
        //重新加载钱包信息
        if (mAssetFragment != null) {
            mAssetFragment.initData();
        }
    }
}
