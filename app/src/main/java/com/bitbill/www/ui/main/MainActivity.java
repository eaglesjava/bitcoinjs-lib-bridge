package com.bitbill.www.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.common.presenter.WalletMvpPresenter;
import com.bitbill.www.common.presenter.WalletMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;
import com.bitbill.www.model.eventbus.SendSuccessEvent;
import com.bitbill.www.model.eventbus.UnConfirmEvent;
import com.bitbill.www.model.eventbus.WalletDeleteEvent;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.Unconfirm;
import com.bitbill.www.ui.main.asset.AssetFragment;
import com.bitbill.www.ui.main.asset.BtcUnconfirmFragment;
import com.bitbill.www.ui.main.contact.ContactFragment;
import com.bitbill.www.ui.main.my.WalletSettingActivity;
import com.bitbill.www.ui.main.receive.ReceiveFragment;
import com.bitbill.www.ui.main.send.SendFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity<MainMvpPresenter>
        implements BaseViewControl, NavigationView.OnNavigationItemSelectedListener, MainMvpView, WalletMvpView, BtcUnconfirmFragment.OnTransactionRecordItemClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Inject
    MainMvpPresenter<WalletModel, MainMvpView> mMainMvpPresenter;
    @Inject
    WalletMvpPresenter<WalletModel, WalletMvpView> mWalletPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private AssetFragment mAssetFragment;
    private ReceiveFragment mReceiveFragment;
    private SendFragment mSendFragment;
    private Socket mSocket;
    private ContactFragment mContactFragment;

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
        addPresenter(mWalletPresenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        setContentView(getLayoutId());
        setUnBinder(ButterKnife.bind(this));
        init(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onBeforeSetContentLayout() {
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
        });
        navView.setNavigationItemSelectedListener(this);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAssetFragment = AssetFragment.newInstance();
        mAdapter.addItem(mAssetFragment);
        mContactFragment = ContactFragment.newInstance(false);
        mAdapter.addItem(mContactFragment);
        mReceiveFragment = ReceiveFragment.newInstance();
        mAdapter.addItem(mReceiveFragment);
        mSendFragment = SendFragment.newInstance();
        mAdapter.addItem(mSendFragment);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                switch (tab.getPosition()) {
                    case 0:
                        setTitle(R.string.title_asset);
                        changeThemeColor(false);
                        break;
                    case 1:
                        setTitle(R.string.title_contact);
                        changeThemeColor(true);
                        break;
                    case 2:
                        setTitle(R.string.title_receive);
                        changeThemeColor(false);
                        break;
                    case 3:
                        setTitle(R.string.title_send);
                        changeThemeColor(false);
                        break;

                }
            }
        });

    }

    private void changeThemeColor(boolean isBlue) {
        if (isBlue) {
            getWindow().setBackgroundDrawableResource(R.drawable.bg_blue);
            mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        } else {
            getWindow().setBackgroundDrawableResource(R.color.windowBackground);
            mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue));
        }
    }

    @Override
    public void initData() {
        mWalletPresenter.loadWallets();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallet_manage) {
            // 切换到钱包管理界面
            WalletSettingActivity.start(this);
        }

        // TODO: 2017/11/17 add other nav item

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnTransactionRecordItemClick(Unconfirm item) {
        //跳转到确认交易详情

    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {

        if (StringUtils.isEmpty(wallets)) finish();//结束主界面
        //设置全局钱包列表对象
        BitbillApp.get().setWallets(wallets);
        reloadWalletInfo();
        //获取钱包余额
        getMvpPresenter().getBalance();
        //加载未确认交易
        getMvpPresenter().listUnconfirm();
    }

    private void reloadWalletInfo() {
        //重新加载钱包信息
        if (mAssetFragment != null) {
            mAssetFragment.lazyData();
        }
        if (mReceiveFragment != null) {
            mReceiveFragment.lazyData();
        }
    }

    @Override
    public void loadWalletsFail() {
        showMessage("加载钱包信息失败");
    }

    @Override
    public List<Wallet> getWallets() {
        return BitbillApp.get().getWallets();
    }

    @Override
    public void getWalletsFail() {
        showMessage("加载钱包信息失败");
    }

    @Override
    public void getBalanceFail() {
        showMessage("获取钱包余额失败");
    }

    @Override
    public void getBalanceSuccess(List<Wallet> wallets) {
        BitbillApp.get().setWallets(wallets);
        reloadWalletInfo();
    }

    @Override
    public void listUnconfirmSuccess(List<Unconfirm> data) {
        if (mAssetFragment != null) {
            mAssetFragment.loadUnconfirm(data);
        }
    }

    @Override
    public void listUnconfirmFail() {
        if (mAssetFragment != null) {
            mAssetFragment.loadUnconfirm(null);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletUpdateSuccess(WalletUpdateEvent walletUpdateEvent) {
        WalletUpdateEvent stickyEvent = EventBus.getDefault().removeStickyEvent(WalletUpdateEvent.class);
        //重新加载钱包信息
        mWalletPresenter.loadWallets();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onSendSuccess(SendSuccessEvent sendSuccessEvent) {
        SendSuccessEvent stickyEvent = EventBus.getDefault().removeStickyEvent(SendSuccessEvent.class);
        //重新加载钱包信息
        if (mSendFragment != null) {
            mSendFragment.sendSuccess();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUnConfirmReceive(UnConfirmEvent unConfirmEvent) {
        UnConfirmEvent stickyEvent = EventBus.getDefault().removeStickyEvent(UnConfirmEvent.class);
        //加载未确认交易
        getMvpPresenter().listUnconfirm();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateContactEvent(ContactUpdateEvent contactUpdateEvent) {
        ContactUpdateEvent stickyEvent = EventBus.getDefault().removeStickyEvent(ContactUpdateEvent.class);
        //重新加载联系人信息
        if (mContactFragment != null) {
            mContactFragment.initData();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletDeleteSuccess(WalletDeleteEvent walletDeleteEvent) {
        WalletDeleteEvent stickyEvent = EventBus.getDefault().removeStickyEvent(WalletDeleteEvent.class);
        Wallet wallet = walletDeleteEvent.getWallet();
        // TODO: 2017/12/28 优化 重新加载钱包信息
        mWalletPresenter.loadWallets();
    }

    public void addContact() {
        mViewPager.setCurrentItem(1, true);
        mContactFragment.showSelectDialog();
    }
}
