package com.bitbill.www.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.base.view.widget.PopupWalletMenu;
import com.bitbill.www.common.base.view.widget.WalletView;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.info.WalletInfoActivity;
import com.bitbill.www.ui.wallet.init.InitWalletActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class AssetFragment extends BaseLazyFragment<AssetMvpPresenter> implements AssetMvpView, WalletView.OnWalletClickListener {


    private static final int BOTTOM_MARGIN = 15;//unit dp
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_btc_amount)
    TextView tvBtcAmount;
    @BindView(R.id.ll_wallet_container)
    LinearLayout llWalletContainer;
    @BindView(R.id.tv_current_wallet_count)
    TextView mWalletCountView;

    @Inject
    AssetMvpPresenter<WalletModel, AssetMvpView> mAssetMvpPresenter;
    private PopupWalletMenu mWalletMenu;
    private int mWalletCount;

    public AssetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AssetFragment.
     */
    public static AssetFragment newInstance() {
        AssetFragment fragment = new AssetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public AssetMvpPresenter getMvpPresenter() {
        return mAssetMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lazyData();
            }
        });
        mWalletMenu = new PopupWalletMenu(getBaseActivity());
        mWalletMenu.setOnWalletMenuItemClickListener(new PopupWalletMenu.OnWalletMenuItemClickListener() {
            @Override
            public void onCreateWallet(View view) {
                //跳转到创建钱包界面
                InitWalletActivity.start(getBaseActivity(), true, false);
                if (mWalletMenu.isShowing()) {
                    mWalletMenu.dismiss();
                }
            }

            @Override
            public void onImportWallet(View view) {
                //跳转到导入钱包界面
                InitWalletActivity.start(getBaseActivity(), false, false);
                if (mWalletMenu.isShowing()) {
                    mWalletMenu.dismiss();
                }

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_asset;
    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {
        if (wallets == null) {
            return;
        }
        //设置全局钱包列表对象
        BitbillApp.get().setWallets(wallets);
        llWalletContainer.removeAllViews();
        mWalletCountView.setText(String.format(getString(R.string.text_asset_current_wallet), wallets.size()));
        for (Wallet wallet : wallets) {
            addWalletView(wallet);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void addWalletView(Wallet wallet) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BOTTOM_MARGIN, getResources().getDisplayMetrics());

        llWalletContainer.addView(
                new WalletView(getBaseActivity())
                        .setWallet(wallet)
                        .setOnWalletClickListener(this), layoutParams);
    }

    @Override
    public void loadWalletsFail() {
        showMessage("加载钱包信息失败，请退出重试");
    }

    @OnClick(R.id.iv_plus)
    public void plusClick(View view) {
        if (mWalletMenu.isShowing()) {
            mWalletMenu.dismiss();
        } else {
            mWalletMenu.show(view);
        }
    }

    @Override
    public void onWalletClick(Wallet wallet, View view) {
        //跳转到钱包详情页
        WalletInfoActivity.start(getBaseActivity(), wallet);
    }

    @Override
    public void onBackupClick(Wallet wallet, View view) {
        //跳转到备份界面
        BackUpWalletActivity.start(getBaseActivity(), wallet);
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {
        getMvpPresenter().loadWallet();
    }
}
