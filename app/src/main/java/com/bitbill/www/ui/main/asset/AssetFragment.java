package com.bitbill.www.ui.main.asset;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.PopupWalletMenu;
import com.bitbill.www.common.widget.WalletView;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.main.my.ShortCutSettingMvpPresenter;
import com.bitbill.www.ui.main.my.ShortCutSettingMvpView;
import com.bitbill.www.ui.main.send.ScanQrcodeActivity;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.importing.ImportWalletActivity;
import com.bitbill.www.ui.wallet.info.WalletInfoActivity;
import com.bitbill.www.ui.wallet.init.CreateWalletIdActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class AssetFragment extends BaseLazyFragment implements WalletView.OnWalletClickListener, ShortCutSettingMvpView {


    private static final int BOTTOM_MARGIN = 15;//unit dp
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_btc_amount)
    TextView tvBtcAmount;
    @BindView(R.id.ll_wallet_container)
    LinearLayout llWalletContainer;
    @BindView(R.id.tv_current_wallet_count)
    TextView mWalletCountView;
    @BindView(R.id.rb_socket_status)
    RadioButton ivSocketStatus;
    @BindView(R.id.ll_short_cut_scan)
    LinearLayout mLlShortCutScan;
    @BindView(R.id.ll_short_cut_contact)
    LinearLayout mLlShortCutContact;
    @BindView(R.id.ll_short_cut)
    LinearLayout mLlShortCut;
    @BindView(R.id.fl_btc_unconfirm)
    FrameLayout mFlBtcUnconfirm;
    @BindView(R.id.iv_plus)
    ImageView mIvPlus;
    @Inject
    ShortCutSettingMvpPresenter<AppModel, ShortCutSettingMvpView> mShortCutSettingMvpPresenter;
    private PopupWalletMenu mWalletMenu;
    private int mWalletCount;
    private boolean isFirstLoading = true;//第一次加载
    private List<Wallet> mWalletList;

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
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
        addPresenter(mShortCutSettingMvpPresenter);
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
                ((MainActivity) getBaseActivity()).initData();
            }
        });
        mWalletMenu = new PopupWalletMenu(getBaseActivity());
        mWalletMenu.setOnWalletMenuItemClickListener(new PopupWalletMenu.OnWalletMenuItemClickListener() {
            @Override
            public void onCreateWallet(View view) {
                //跳转到创建钱包界面
                CreateWalletIdActivity.start(getBaseActivity(), null, true, true);
                if (mWalletMenu.isShowing()) {
                    mWalletMenu.dismiss();
                }
            }

            @Override
            public void onImportWallet(View view) {
                //跳转到导入钱包界面
                ImportWalletActivity.start(getBaseActivity(), true);
                if (mWalletMenu.isShowing()) {
                    mWalletMenu.dismiss();
                }

            }
        });
    }

    @Override
    public void initData() {
        ivSocketStatus.setChecked(BitbillApp.get().getSocketConnected());
    }

    @Override
    public void showLoading() {
        if (isFirstLoading) {
            isFirstLoading = false;
            return;
        }
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

    private void addWalletView(Wallet wallet) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BOTTOM_MARGIN, getResources().getDisplayMetrics());

        llWalletContainer.addView(
                new WalletView(getBaseActivity())
                        .setWallet(wallet)
                        .setOnWalletClickListener(this), layoutParams);
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
        BackUpWalletActivity.start(getBaseActivity(), wallet, false);
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {
        if (mWalletList == null) {
            mWalletList = new ArrayList<>();
        }
        mWalletList.clear();
        mWalletList.addAll(BitbillApp.get().getWallets());
        if (llWalletContainer != null) {

            llWalletContainer.removeAllViews();
            mWalletCountView.setText(String.format(getString(R.string.text_asset_current_wallet), mWalletList.size()));
            for (Wallet wallet : mWalletList) {
                addWalletView(wallet);
            }
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    public void loadUnconfirm(List<TxRecord> unconfirmList) {
        if (StringUtils.isEmpty(unconfirmList)) {
            //如果加载不到未确认列表 移除BtcUnconfirmFragment
            Fragment fragment = getChildFragmentManager().findFragmentByTag(BtcUnconfirmFragment.TAG);
            if (fragment != null) {
                getChildFragmentManager().beginTransaction().remove(fragment);
            }
        } else {
            if (isAdded()) {

                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_btc_unconfirm, BtcUnconfirmFragment.newInstance((ArrayList<TxRecord>) unconfirmList), BtcUnconfirmFragment.TAG)
                        .commit();
            }
        }
    }

    @OnClick({R.id.ll_short_cut_scan, R.id.ll_short_cut_contact, R.id.ll_short_cut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_short_cut_scan:
                //打开扫一扫界面
                ScanQrcodeActivity.start(getBaseActivity(), true);
                break;
            case R.id.ll_short_cut_contact:
                //跳转到联系人界面并弹出选择框
                ((MainActivity) getBaseActivity()).addContact();
                break;
        }
    }

    public void setShortcutShown(boolean shortcutShown) {
        mLlShortCut.setVisibility(shortcutShown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        setShortcutShown(mShortCutSettingMvpPresenter.isShortcutShown());
    }
}
