package com.bitbill.www.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.widget.PopupWalletMenu;
import com.bitbill.www.common.base.view.widget.WalletView;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
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
public class AssetFragment extends BaseFragment<AssetMvpPresenter> implements AssetMvpView, WalletView.OnBackupClickListener {


    private static final int BOTTOM_MARGIN = 15;//unit dp
    @BindView(R.id.tv_btc_amount)
    TextView tvBtcAmount;
    @BindView(R.id.ll_wallet_container)
    LinearLayout llWalletContainer;

    @Inject
    AssetMvpPresenter<WalletModel, AssetMvpView> mAssetMvpPresenter;
    private PopupWalletMenu mWalletMenu;

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
        mWalletMenu = new PopupWalletMenu(getBaseActivity());
        mWalletMenu.setOnWalletMenuItemClickListener(new PopupWalletMenu.OnWalletMenuItemClickListener() {
            @Override
            public void onCreateWallet(View view) {
                //跳转到创建钱包界面
                InitWalletActivity.start(getBaseActivity(), true);
            }

            @Override
            public void onImportWallet(View view) {
                //跳转到导入钱包界面
                InitWalletActivity.start(getBaseActivity(), false);

            }
        });
    }

    @Override
    public void initData() {
        getMvpPresenter().loadWallet();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_asset;
    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {
        for (Wallet wallet : wallets) {
            addWalletView(wallet);
        }
    }

    private void addWalletView(Wallet wallet) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BOTTOM_MARGIN, getResources().getDisplayMetrics());

        llWalletContainer.addView(
                new WalletView(getBaseActivity())
                        .setWallet(wallet)
                        .setOnBackupClickListener(this), layoutParams);
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
    public void onBackupClick(Wallet wallet, View view) {
        //跳转到备份界面
        BackUpWalletActivity.start(getBaseActivity(), wallet);
    }
}
