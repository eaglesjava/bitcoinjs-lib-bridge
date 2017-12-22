package com.bitbill.www.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.model.entity.eventbus.SendSuccessEvent;
import com.bitbill.www.model.entity.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.TransactionRecord;
import com.bitbill.www.ui.main.asset.AssetFragment;
import com.bitbill.www.ui.main.asset.BtcUnconfirmFragment;
import com.bitbill.www.ui.main.contact.ContactActivity;
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
import io.socket.emitter.Emitter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity<MainMvpPresenter>
        implements BaseViewControl, NavigationView.OnNavigationItemSelectedListener, MainMvpView, BtcUnconfirmFragment.OnTransactionRecordItemClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Inject
    MainMvpPresenter<WalletModel, MainMvpView> mMainMvpPresenter;
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
    private ReceiveFragment mReceiveFragment;
    private SendFragment mSendFragment;
    private Socket mSocket;

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
        onBeforeSetContentLayout();
        setContentView(getLayoutId());
        setUnBinder(ButterKnife.bind(this));
        init(savedInstanceState);
        initView();
        initData();
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

            ContactActivity.start(this);
        }

        // TODO: 2017/11/17 add other nav item

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnTransactionRecordItemClick(TransactionRecord item) {

    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {

        if (wallets == null) {
            return;
        }
        //设置全局钱包列表对象
        BitbillApp.get().setWallets(wallets);
        reloadWalletInfo();
        getMvpPresenter().getBalance();
    }

    private void reloadWalletInfo() {
        //重新加载钱包信息
        if (mAssetFragment != null) {
            mAssetFragment.initData();
        }
        if (mReceiveFragment != null) {
            mReceiveFragment.initData();
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletUpdateSuccess(WalletUpdateEvent walletUpdateEvent) {
        WalletUpdateEvent stickyEvent = EventBus.getDefault().removeStickyEvent(WalletUpdateEvent.class);
        //重新加载钱包信息
        getMvpPresenter().loadWallet();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onSendSuccess(SendSuccessEvent sendSuccessEvent) {
        SendSuccessEvent stickyEvent = EventBus.getDefault().removeStickyEvent(SendSuccessEvent.class);
        //重新加载钱包信息
        if (mSendFragment != null) {
            mSendFragment.sendSuccess();
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

    @Override
    public void onBeforeSetContentLayout() {
        mSocket = BitbillApp.get().getSocket();

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                mSocket.emit("register", "{\"walletId\":\"BitbillTest\",\"deviceToken\":\"\",\"platform\":\"iOS\",\"clientId\":\"C03CEAA2-9498-4201-9AD2-04A3C01C8F51\"}");
                Log.d(TAG, "EVENT_CONNECT() called with: args = [" + args + "]");
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                Log.d(TAG, "EVENT_DISCONNECT() called with: args = [" + args + "]");
            }

        }).on("confirm", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "confirm called with: args = [" + args + "]");
            }
        }).on("unconfirm", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d(TAG, "unconfirm called with: args = [" + args + "]");
            }
        });
        mSocket.connect();
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
        mReceiveFragment = ReceiveFragment.newInstance();
        mAdapter.addItem(mReceiveFragment);
        mSendFragment = SendFragment.newInstance();
        mAdapter.addItem(mSendFragment);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void initData() {
        getMvpPresenter().loadWallet();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
