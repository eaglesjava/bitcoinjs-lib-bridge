package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.base.view.dialog.MessageConfirmDialog;
import com.bitbill.www.common.base.view.widget.SelectWalletView;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiveFragment extends BaseLazyFragment<ReceiveMvpPresenter> {

    private static final String TAG = "ReceiveFragment";
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.wv_select)
    SelectWalletView selectWalletView;
    @Inject
    ReceiveMvpPresenter<AppModel, ReceiveMvpView> mReceiveMvpPresenter;
    private FragmentAdapter mFragmentAdapter;
    private BtcReceiveFragment mBtcReceiveFragment;
    private List<Wallet> mWalletList;
    private WalletSelectDialog mWalletSelectDialog;
    private Wallet mSelectedWallet;
    private int mSelectedPosition = -1;

    public ReceiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReceiveFragment.
     */
    public static ReceiveFragment newInstance() {
        ReceiveFragment fragment = new ReceiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public ReceiveMvpPresenter getMvpPresenter() {
        return mReceiveMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {
        setHasOptionsMenu(true);
        mWalletList = new ArrayList<>();

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mWalletSelectDialog = WalletSelectDialog.newInstance();
        mWalletSelectDialog.setOnWalletSelectItemClickListener(new WalletSelectDialog.OnWalletSelectItemClickListener() {
            @Override
            public void onItemSelected(Wallet selectedWallet, int position) {
                mSelectedWallet = selectedWallet;
                mSelectedPosition = position;
                //刷新选择布局
                selectWalletView.setWallet(selectedWallet);
                loadBtcAddress();
            }
        });

        selectWalletView.setOnWalletClickListener(new SelectWalletView.OnWalletClickListener() {
            @Override
            public void onWalletClick(Wallet wallet, View view) {
                //弹出钱包选择界面
                mWalletSelectDialog.show(getChildFragmentManager(), WalletSelectDialog.TAG);

            }

            @Override
            public void onBackupClick(Wallet wallet, View view) {
                //跳转到备份钱包界面
                BackUpWalletActivity.start(getBaseActivity(), wallet);
            }
        });
        setupViewPager();

    }


    private void setupViewPager() {
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        mBtcReceiveFragment = BtcReceiveFragment.newInstance();
        mFragmentAdapter.addItem("btc", mBtcReceiveFragment);
        mFragmentAdapter.addItem("bch", BchInfoFragment.newInstance());
        mFragmentAdapter.addItem("eth", EthInfoFragment.newInstance());
        viewPager.setAdapter(mFragmentAdapter);
        tabs.setupWithViewPager(viewPager);
        //禁止tab选择
        LinearLayout tabStrip = (LinearLayout) tabs.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null) {
                tabView.setClickable(false);
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_receive;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!getMvpPresenter().isRemindDialogShown()) {
                MessageConfirmDialog.newInstance(getString(R.string.dialog_title_friendly_remind),
                        getString(R.string.dialog_msg_change_address),
                        getString(R.string.dialog_btn_known),
                        true)
                        .show(getChildFragmentManager(), MessageConfirmDialog.TAG);
                getMvpPresenter().setRemindDialogShown();
            }
        }
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {
        mWalletList.clear();
        mWalletList.addAll(BitbillApp.get().getWallets());
        //重置选中的钱包对象
        if (mSelectedPosition == -1 || mSelectedPosition > mWalletList.size() - 1) {
            // 选择默认的钱包对象作为选中的
            mSelectedWallet = BitbillApp.get().getDefaultWallet();
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
            selectWalletView.setWallet(mSelectedWallet);
            selectWalletView.setVisibility(View.VISIBLE);
            loadBtcAddress();
        } else {
            selectWalletView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.receive_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            // 刷新接收地址
            refreshBtcAddress();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshBtcAddress() {
        if (mBtcReceiveFragment != null) {
            mBtcReceiveFragment.refreshAddress(mSelectedWallet);
        }
    }

    private void loadBtcAddress() {
        if (mBtcReceiveFragment != null) {
            mBtcReceiveFragment.loadAddress(mSelectedWallet);
        }
    }
}
