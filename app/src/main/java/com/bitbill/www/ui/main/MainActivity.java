package com.bitbill.www.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.common.presenter.GetCacheVersionMvpPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpView;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.common.presenter.WalletMvpPresenter;
import com.bitbill.www.common.presenter.WalletMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;
import com.bitbill.www.model.eventbus.SendSuccessEvent;
import com.bitbill.www.model.eventbus.UnConfirmEvent;
import com.bitbill.www.model.eventbus.WalletDeleteEvent;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.asset.AssetFragment;
import com.bitbill.www.ui.main.asset.BtcUnconfirmFragment;
import com.bitbill.www.ui.main.contact.ContactFragment;
import com.bitbill.www.ui.main.my.AboutUsActivity;
import com.bitbill.www.ui.main.my.ContactSettingActivity;
import com.bitbill.www.ui.main.my.ShortCutSettingActivity;
import com.bitbill.www.ui.main.my.SystemSettingActivity;
import com.bitbill.www.ui.main.my.WalletSettingActivity;
import com.bitbill.www.ui.main.receive.ReceiveFragment;
import com.bitbill.www.ui.main.send.SendFragment;
import com.bitbill.www.ui.wallet.info.transfer.TransferDetailsActivity;

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
        implements BaseViewControl, NavigationView.OnNavigationItemSelectedListener, MainMvpView, WalletMvpView, ParseTxInfoMvpView, GetCacheVersionMvpView, SyncAddressMvpView, BtcUnconfirmFragment.OnTransactionRecordItemClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int INDEX_ASSET = 0;
    private static final int INDEX_CONTACT = 1;
    private static final int INDEX_RECEIVE = 2;
    private static final int INDEX_SEND = 3;

    @Inject
    MainMvpPresenter<WalletModel, MainMvpView> mMainMvpPresenter;
    @Inject
    WalletMvpPresenter<WalletModel, WalletMvpView> mWalletPresenter;
    @Inject
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> mParseTxInfoMvpPresenter;
    @Inject
    GetCacheVersionMvpPresenter<WalletModel, GetCacheVersionMvpView> mGetCacheVersionMvpPresenter;
    @Inject
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> mSyncAddressMvpPresentder;

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
    private Contact mSendContact;
    private int index = INDEX_ASSET;
    private List<TxElement> mTxInfoList;
    private String mAddress;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void start(Context context, Contact sendContact, String address) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstants.EXTRA_CONTACT, sendContact);
        intent.putExtra(AppConstants.EXTRA_ADDRESS, address);
        context.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mSendContact = ((Contact) intent.getSerializableExtra(AppConstants.EXTRA_CONTACT));
        if (mSendContact != null) {
            //切换到发送联系人界面
            mViewPager.setCurrentItem(INDEX_SEND, false);
            if (mSendFragment != null) {
                mSendFragment.setSendAddress(mSendContact);
            }
        } else {

            mAddress = intent.getStringExtra(AppConstants.EXTRA_ADDRESS);
            if (StringUtils.isNotEmpty(mAddress)) {
                //切换到发送界面
                mViewPager.setCurrentItem(INDEX_SEND, false);
                mSendFragment.setSendAddress(mAddress);
            }
        }
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
        addPresenter(mParseTxInfoMvpPresenter);
        addPresenter(mGetCacheVersionMvpPresenter);
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
        setTitle(R.string.title_activity_main);
    }

    @Override
    public void onBeforeSetContentLayout() {
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        setUpDrawerLayout();

        setUpViewpager();
    }

    private void setUpDrawerLayout() {
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
    }

    private void setUpViewpager() {
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
                index = tab.getPosition();
                switch (tab.getPosition()) {
                    case INDEX_ASSET:
                        setTitle(R.string.title_asset);
                        changeThemeColor(false);
                        break;
                    case INDEX_CONTACT:
                        setTitle(R.string.title_contact);
                        changeThemeColor(true);
                        break;
                    case INDEX_RECEIVE:
                        setTitle(R.string.title_receive);
                        changeThemeColor(false);
                        break;
                    case INDEX_SEND:
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
        if (!StringUtils.isEmpty(getApp().getWallets())) {
            loadWalletsSuccess(getApp().getWallets());
        } else {
            mWalletPresenter.loadWallets();
        }
        mGetCacheVersionMvpPresenter.getCacheVersion();
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
        } else if (id == R.id.nav_contact) {
            // 切换到钱包管理界面
            ContactSettingActivity.start(this);
        } else if (id == R.id.nav_shortcut) {
            // 切换到快捷方式设置界面
            ShortCutSettingActivity.start(this);
        } else if (id == R.id.nav_sys_setting) {
            // 切换到系统设置界面
            SystemSettingActivity.start(this);
        } else if (id == R.id.nav_about_us) {
            // 切换到关于我们界面
            AboutUsActivity.start(this);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnTransactionRecordItemClick(TxRecord item) {
        //跳转到确认交易详情
        TransferDetailsActivity.start(MainActivity.this, item);
    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {

        if (StringUtils.isEmpty(wallets)) {
            //跳转到引导页面
            GuideActivity.start(MainActivity.this);
            //结束主界面
            finish();
        }

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
        showMessage(R.string.fail_get_wallet_info);
    }

    @Override
    public List<Wallet> getWallets() {
        return BitbillApp.get().getWallets();
    }

    @Override
    public void getWalletsFail() {
        showMessage(R.string.fail_get_wallet_info);
    }

    @Override
    public void getBalanceFail() {

    }

    @Override
    public void getBalanceSuccess(List<Wallet> wallets, Long totalAmount) {
        BitbillApp.get().setWallets(wallets);

        //设置btc总额
        if (mAssetFragment != null) {
            mAssetFragment.setBtcTotalAmount(totalAmount);
        }
        reloadWalletInfo();
    }

    @Override
    public void listUnconfirmSuccess(List<TxElement> data) {
        loadUnconfrim(data);
    }

    private void loadUnconfrim(List<TxElement> data) {
        if (StringUtils.isEmpty(data)) {
            parsedTxItemList(null);
        } else {

            mTxInfoList = data;
            mParseTxInfoMvpPresenter.parseTxInfo();
        }

    }

    private void loadParsedUnconfirmList(List<TxRecord> data) {
        if (mAssetFragment != null) {
            mAssetFragment.loadUnconfirm(data);
        }
    }

    @Override
    public void listUnconfirmFail() {
        loadParsedUnconfirmList(null);
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
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.VIBRATE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.dialog_msg_request_permissions), REQUEST_CODE_QRCODE_PERMISSIONS, perms);
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

    @Override
    public List<TxElement> getTxInfoList() {
        return mTxInfoList;
    }

    @Override
    public void requireTxInfoList() {
    }

    @Override
    public void getTxInfoListFail() {

    }

    @Override
    public void parsedTxItemList(List<TxRecord> txRecords) {
        loadParsedUnconfirmList(txRecords);
    }

    @Override
    public void parsedTxItemListFail() {
        showMessage(R.string.fail_parse_tx_item);
    }

    @Override
    public void getResponseAddressIndex(long indexNo, Wallet wallet) {
        mSyncAddressMvpPresentder.syncLastAddressIndex(indexNo, wallet);
    }

    @Override
    public void getDiffVersionWallets(List<Wallet> tmpWalletList) {

        // TODO: 2018/1/8 只更新更改的wallet

    }

    @Override
    public void showLoading() {
        if (mAssetFragment != null) {
            mAssetFragment.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mAssetFragment != null) {
            mAssetFragment.hideLoading();
        }
    }
}
