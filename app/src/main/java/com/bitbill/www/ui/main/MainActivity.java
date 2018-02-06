package com.bitbill.www.ui.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseActivity;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.common.presenter.BalanceMvpPresenter;
import com.bitbill.www.common.presenter.BalanceMvpView;
import com.bitbill.www.common.presenter.GetCacheVersionMvpPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpView;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.common.presenter.UpdateMvpPresenter;
import com.bitbill.www.common.presenter.UpdateMvpView;
import com.bitbill.www.common.presenter.WalletMvpPresenter;
import com.bitbill.www.common.presenter.WalletMvpView;
import com.bitbill.www.common.utils.AnimationUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.Decoration;
import com.bitbill.www.common.widget.dialog.BaseConfirmDialog;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.common.widget.dialog.UpdateAppDialog;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.eventbus.ConfirmedEvent;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;
import com.bitbill.www.model.eventbus.SendSuccessEvent;
import com.bitbill.www.model.eventbus.SocketServerStateEvent;
import com.bitbill.www.model.eventbus.UnConfirmEvent;
import com.bitbill.www.model.eventbus.WalletDeleteEvent;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.socket.Confirmed;
import com.bitbill.www.model.wallet.network.socket.ContextBean;
import com.bitbill.www.model.wallet.network.socket.UnConfirmed;
import com.bitbill.www.service.SocketServiceProvider;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.asset.AssetFragment;
import com.bitbill.www.ui.main.asset.BtcUnconfirmFragment;
import com.bitbill.www.ui.main.contact.ContactFragment;
import com.bitbill.www.ui.main.my.AboutUsActivity;
import com.bitbill.www.ui.main.my.ContactSettingActivity;
import com.bitbill.www.ui.main.my.FeedbackActivity;
import com.bitbill.www.ui.main.my.ShortCutSettingActivity;
import com.bitbill.www.ui.main.my.SystemSettingActivity;
import com.bitbill.www.ui.main.my.WalletSettingActivity;
import com.bitbill.www.ui.main.receive.ReceiveFragment;
import com.bitbill.www.ui.main.receive.ScanPayActivity;
import com.bitbill.www.ui.main.send.SendFragment;
import com.bitbill.www.ui.wallet.info.BtcRecordMvpPresenter;
import com.bitbill.www.ui.wallet.info.BtcRecordMvpView;
import com.bitbill.www.ui.wallet.info.transfer.TransferDetailsActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.socket.client.Socket;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity<MainMvpPresenter>
        implements BaseViewControl, NavigationView.OnNavigationItemSelectedListener, MainMvpView, BalanceMvpView, WalletMvpView, ParseTxInfoMvpView, GetCacheVersionMvpView, SyncAddressMvpView, BtcRecordMvpView, UpdateMvpView, BtcUnconfirmFragment.OnTransactionRecordItemClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int INDEX_ASSET = 0;
    private static final int INDEX_CONTACT = 1;
    private static final int INDEX_RECEIVE = 2;
    private static final int INDEX_SEND = 3;

    @Inject
    MainMvpPresenter<TxModel, MainMvpView> mMainMvpPresenter;
    @Inject
    BalanceMvpPresenter<WalletModel, BalanceMvpView> mBalanceMvpPresenter;
    @Inject
    WalletMvpPresenter<WalletModel, WalletMvpView> mWalletPresenter;
    @Inject
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> mParseTxInfoMvpPresenter;
    @Inject
    GetCacheVersionMvpPresenter<WalletModel, GetCacheVersionMvpView> mGetCacheVersionMvpPresenter;
    @Inject
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> mSyncAddressMvpPresentder;
    @Inject
    BtcRecordMvpPresenter<TxModel, BtcRecordMvpView> mBtcRecordMvpPresenter;
    @Inject
    UpdateMvpPresenter<AppModel, UpdateMvpView> mUpdateMvpPresenter;

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
    @BindView(R.id.fl_bottom_sheet)
    FrameLayout bottomSheetView;
    @BindView(R.id.v_mask)
    View mMaskView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private CommonAdapter<Wallet> mSelectWalletAdapter;
    private List<Wallet> mWalletList = new ArrayList<>();

    private FragmentAdapter mAdapter;
    private AssetFragment mAssetFragment;
    private ReceiveFragment mReceiveFragment;
    private SendFragment mSendFragment;
    private Socket mSocket;
    private ContactFragment mContactFragment;
    private Contact mSendContact;
    private int index = INDEX_ASSET;
    private String mAddress;
    private int mSelectedPosition;
    private SocketServiceProvider mBoundService;
    protected ServiceConnection socketConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((SocketServiceProvider.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };
    private UpdateAppDialog mUpdateAppDialog;
    private MessageConfirmDialog mUpdateMsgConfirmDialog;
    private String mFromTag;
    private boolean isListUnconfirm;
    private Wallet mSelectedWallet;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void start(Context context, String fromTag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstants.EXTRA_FROM_TAG, fromTag);
        context.startActivity(intent);
    }

    public static void start(Context context, Contact sendContact, String address) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstants.EXTRA_CONTACT, sendContact);
        intent.putExtra(AppConstants.EXTRA_ADDRESS, address);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mSendContact = ((Contact) intent.getSerializableExtra(AppConstants.EXTRA_CONTACT));
        mAddress = intent.getStringExtra(AppConstants.EXTRA_ADDRESS);
        isListUnconfirm = intent.getBooleanExtra(AppConstants.EXTRA_LIST_UNCONFIRM, false);
        mFromTag = intent.getStringExtra(AppConstants.EXTRA_FROM_TAG);

        if (ScanPayActivity.TAG.equals(mFromTag)) {
            //切回资产页面
            if (mViewPager != null) {
                mViewPager.setCurrentItem(INDEX_ASSET, false);
            }
            return;
        }
        if (mSendContact != null) {
            //切换到发送联系人界面
            mViewPager.setCurrentItem(INDEX_SEND, false);
            if (mSendFragment != null) {
                mSendFragment.setSendAddress(mSendContact);
            }
        } else if (StringUtils.isNotEmpty(mAddress)) {
            //切换到发送界面
            mViewPager.setCurrentItem(INDEX_SEND, false);
            if (mSendFragment != null) {
                mSendFragment.setSendAddress(mAddress);
            }
        }

        if (isListUnconfirm) {
            //获取未确认列表
            getMvpPresenter().listUnconfirm();

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
        addPresenter(mBalanceMvpPresenter);
        addPresenter(mWalletPresenter);
        addPresenter(mParseTxInfoMvpPresenter);
        addPresenter(mGetCacheVersionMvpPresenter);
        addPresenter(mBtcRecordMvpPresenter);
        addPresenter(mUpdateMvpPresenter);
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
        doBindService();
    }

    private void doBindService() {
        if (mBoundService == null) {
            bindService(new Intent(MainActivity.this, SocketServiceProvider.class), socketConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(socketConnection);
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

        setupBottomSheet();
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
                //清空临时数据
                if (mSendFragment != null) {
                    mSendFragment.clearData();
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

    /**
     * todo 抽离成单独view
     */
    private void setupBottomSheet() {
        mMaskView.setOnClickListener(v -> {
            hideSelectWallet();
        });
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        Decoration decor = new Decoration(this, Decoration.VERTICAL);
        mRecyclerView.addItemDecoration(decor);

        mSelectWalletAdapter = new CommonAdapter<Wallet>(this, R.layout.item_wallet_select_view, mWalletList) {

            @Override
            protected void convert(ViewHolder holder, Wallet wallet, final int position) {

                holder.setText(R.id.tv_wallet_name, StringUtils.cutWalletName(wallet.getName()));
                holder.setText(R.id.tv_wallet_amount, StringUtils.satoshi2btc(wallet.getBalance()) + " btc");
                holder.setText(R.id.tv_wallet_label, String.valueOf(wallet.getName().charAt(0)));

                holder.setChecked(R.id.rb_selector, wallet.isSelected());

                holder.itemView.setOnClickListener(v -> {
                    //实现单选方法三： RecyclerView另一种定向刷新方法：不会有白光一闪动画 也不会重复onBindVIewHolder
                    //如果勾选的不是已经勾选状态的Item
                    if (mSelectedPosition != position && mSelectedPosition != -1) {
                        //先取消上个item的勾选状态
                        ViewHolder commonHolder = ((ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mSelectedPosition));
                        if (commonHolder != null) {//还在屏幕里
                            commonHolder.setChecked(R.id.rb_selector, false);
                        } else {
                            //add by 2016 11 22 for 一些极端情况，holder被缓存在Recycler的cacheView里，
                            //此时拿不到ViewHolder，但是也不会回调onBindViewHolder方法。所以add一个异常处理
                            notifyItemChanged(mSelectedPosition);
                        }
                        //不管在不在屏幕里 都需要改变数据
                        //设置新Item的勾选状态
                        mSelectedPosition = position;
                        mWalletList.get(mSelectedPosition).setSelected(true);
                        mSelectedWallet = wallet;
                        holder.setChecked(R.id.rb_selector, wallet.isSelected());
                        if (mReceiveFragment != null) {
                            mReceiveFragment.setSelectedWallet(wallet);
                        }
                    }
                    // dismiss
                    hideSelectWallet();
                });

            }
        };
        mRecyclerView.setAdapter(mSelectWalletAdapter);
        bottomSheetView.setOnClickListener(v -> {
            hideSelectWallet();
        });
    }

    private void hideSelectWallet() {
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        if (mMaskView != null) {
            AnimationUtils.getAlphaAnimation(1.0f, 0.0f, 200).start();
            mMaskView.setVisibility(View.GONE);
        }
    }

    public void showSelectWallet() {  //弹出钱包选择界面
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        if (mMaskView != null) {
            AnimationUtils.getAlphaAnimation(0.0f, 1.0f, 200).start();
            mMaskView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        mUpdateMvpPresenter.checkUpdate();
        mWalletPresenter.loadWallets();
        mGetCacheVersionMvpPresenter.getCacheVersion();
        if (mBoundService != null) {
            setSocketStatus(mBoundService.getSocketStatus());
        }
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
        } else if (id == R.id.nav_fee_back) {
            // 切换到关于我们界面
            FeedbackActivity.start(this);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnTransactionRecordItemClick(TxRecord item) {
        //跳转到未确认交易详情
        TransferDetailsActivity.start(MainActivity.this, item, TAG);
    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {

        if (StringUtils.isEmpty(wallets)) {
            //跳转到引导页面
            GuideActivity.start(MainActivity.this);
            //结束主界面
            finish();
        }
        reloadWalletInfo(wallets);
        //获取钱包余额
        mBalanceMvpPresenter.getBalance();
        //加载未确认交易
        getMvpPresenter().listUnconfirm();
    }

    private void reloadWalletInfo(List<Wallet> wallets) {
        //设置全局钱包列表对象
        getApp().setWallets(wallets);
        mWalletList.clear();
        mWalletList.addAll(wallets);

        //重置钱包选择postion
        if (mSelectedPosition == -1 || mSelectedPosition > mWalletList.size() - 1) {
            // 选择默认的钱包对象作为选中的
            mSelectedWallet = BitbillApp.get().getDefaultWallet();
            mSelectedPosition = mWalletList.indexOf(mSelectedWallet);
        } else {
            mSelectedWallet = mWalletList.get(mSelectedPosition);
        }
        if (mSelectedWallet != null) {
            //重置单选select对象
            for (Wallet wallet : mWalletList) {
                if (wallet.equals(mSelectedWallet)) {
                    wallet.setSelected(true);
                } else {
                    wallet.setSelected(false);
                }
            }
            if (mReceiveFragment != null) {
                mReceiveFragment.setSelectedWallet(mSelectedWallet);
            }
        }
        if (mSelectWalletAdapter != null) {
            mSelectWalletAdapter.notifyDataSetChanged();
        }

        //设置钱包总资产 未确认的金额累加
        long totalAmount = 0;
        for (Wallet wallet : mWalletList) {
            totalAmount += wallet.getBalance();
            totalAmount += wallet.getUnconfirm();
        }

        //设置btc总额
        if (mAssetFragment != null) {
            mAssetFragment.setBtcTotalAmount(totalAmount);
        }
        //重新加载钱包信息
        if (mAssetFragment != null) {
            mAssetFragment.lazyData();
        }
        if (mReceiveFragment != null) {
            mReceiveFragment.lazyData();
        }
        //通知钱包选择界面数据刷新
        if (mSelectWalletAdapter != null) {
            mSelectWalletAdapter.notifyDataSetChanged();
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

        reloadWalletInfo(wallets);
    }

    @Override
    public void listUnconfirmSuccess(List<TxElement> data) {
        if (StringUtils.isEmpty(data)) {
            loadUnconfirmList(null);
        } else {
            mParseTxInfoMvpPresenter.parseTxInfo(data);
        }
    }

    private void loadUnconfirmList(List<TxRecord> data) {
        if (mAssetFragment != null && mAssetFragment.isAdded()) {
            mAssetFragment.loadUnconfirm(data);
        }
    }

    @Override
    public void listUnconfirmFail() {
    }

    @Override
    public void loadUnconfirmSuccess(List<TxRecord> txRecords) {
        loadUnconfirmList(txRecords);
    }

    @Override
    public void loadUnconfirmFail() {
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            hideSelectWallet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
        JPushInterface.requestPermission(this);
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
        //切换到资产页面并刷新未确认列表
        if (mViewPager != null) {
            mViewPager.setCurrentItem(INDEX_ASSET);
        }
        if (getMvpPresenter() != null) {
            getMvpPresenter().listUnconfirm();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUnConfirmReceive(UnConfirmEvent unConfirmEvent) {
        UnConfirmEvent stickyEvent = EventBus.getDefault().removeStickyEvent(UnConfirmEvent.class);
        //加载未确认交易
        getMvpPresenter().listUnconfirm();
        mBalanceMvpPresenter.getBalance();

        UnConfirmed unConfirmed = (UnConfirmed) unConfirmEvent.getData();
        if (unConfirmed != null && unConfirmed.getContext() != null) {

            ContextBean context = unConfirmed.getContext();

            updateLocalCache(context);

        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onConfirmedEvent(ConfirmedEvent confirmedEvent) {
        EventBus.getDefault().removeStickyEvent(UnConfirmEvent.class);
        //加载未确认交易
        getMvpPresenter().listUnconfirm();
        mBalanceMvpPresenter.getBalance();

        Confirmed confirmed = (Confirmed) confirmedEvent.getData();
        if (confirmed != null && confirmed.getContext() != null) {

            ContextBean context = confirmed.getContext();
            updateLocalCache(context);

        }
    }

    private void updateLocalCache(ContextBean context) {
        if (!StringUtils.isEmpty(mWalletList)) {
            for (Wallet wallet : mWalletList) {
                if (wallet.getName().equals(context.getWalletId())) {
                    getResponseAddressIndex(context.getIndexNo(), context.getChangeIndexNo(), wallet);
                    if (wallet.getVersion() != context.getVersion()) {
                        List<Wallet> tmpWalletList = new ArrayList<>();
                        tmpWalletList.add(wallet);
                        //重新获取交易列表
                        getDiffVersionWallets(tmpWalletList);
                    }
                }
            }
        }
        if (context.getHeight() > 0) {
            getApp().setBlockHeight(context.getHeight());
        }
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

    private void setSocketStatus(SocketServerStateEvent.ServerState socketStatus) {
        if (mAssetFragment != null && socketStatus != null) {
            mAssetFragment.setSocketStatus(SocketServerStateEvent.ServerState.connected.equals(socketStatus));
        }
    }

    public void addContact() {
        mViewPager.setCurrentItem(1, true);
        mContactFragment.showSelectDialog();
    }

    @Override
    public void requireTxInfoList() {
    }

    @Override
    public void getTxInfoListFail() {
    }

    @Override
    public void parsedTxItemList(List<TxRecord> txRecords) {
        //移除heigh不为-1的交易记录
        getMvpPresenter().loadUnConfirmedList();
    }

    @Override
    public void parsedTxItemListFail() {
        showMessage(R.string.fail_parse_tx_item);
    }

    @Override
    public void getResponseAddressIndex(long indexNo, long changeIndexNo, Wallet wallet) {
        mSyncAddressMvpPresentder.syncLastAddressIndex(indexNo, changeIndexNo, wallet);
    }

    @Override
    public void getDiffVersionWallets(List<Wallet> tmpWalletList) {
        if (!StringUtils.isEmpty(tmpWalletList)) {
            for (Wallet wallet : tmpWalletList) {

                // 只更新更改的wallet的交易记录
                mBtcRecordMvpPresenter.requestTxRecord(wallet);
            }
        }

    }

    @Override
    public void getBlockHeight(long blockheight) {

    }

    @Override
    public void showLoading() {
        if (mUpdateMsgConfirmDialog != null) {
            if (mUpdateMsgConfirmDialog.isShowing()) {
                return;
            }
        }
        if (mUpdateAppDialog != null) {
            if (mUpdateAppDialog.isShowing()) {
                return;
            }
        }
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

    @Override
    public void getWalletFail() {

    }

    @Override
    public void getTxRecordSuccess(List<TxElement> list) {
        if (!StringUtils.isEmpty(list)) {
            mParseTxInfoMvpPresenter.parseTxInfo(list);
        }
    }

    @Override
    public void getTxRecordFail() {

    }

    @Override
    public void loadTxRecordSuccess(List<TxRecord> txRecordList) {

    }

    @Override
    public void needUpdateApp(boolean needUpdate, boolean needForce, String updateVersion, String apkUrl) {
        if (needUpdate) {
            //弹出更新提示框
            if (mUpdateMsgConfirmDialog == null) {
                mUpdateMsgConfirmDialog = MessageConfirmDialog.newInstance(getString(R.string.dialog_title_update_app), getString(R.string.dialog_msg_latest_version) + updateVersion, getString(R.string.dialog_btn_update), needForce, false);
                mUpdateMsgConfirmDialog
                        .setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                                    mViewPager.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //根据url类型跳转
                                            if (!StringUtils.isApkUrl(apkUrl)) {
                                                //跳转到官网
                                                UIHelper.openBrower(MainActivity.this, apkUrl);
                                                if (needForce) finish();
                                            } else {
                                                //弹出下载对话框
                                                mUpdateAppDialog = UpdateAppDialog.newInstance(getString(R.string.dialog_title_download_app), apkUrl);
                                                mUpdateAppDialog
                                                        .show(getSupportFragmentManager());
                                            }
                                        }
                                    }, 500);

                                }
                            }
                        });
            }

            mUpdateMsgConfirmDialog.show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
        }

    }

    @Override
    public void getConfigSuccess(String aversion, String aforceVersion) {

    }

    @Override
    public void getConfigFail() {

    }
}
