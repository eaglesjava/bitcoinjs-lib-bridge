package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.Decoration;
import com.bitbill.www.common.widget.SelectWalletView;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.fl_bottom_sheet)
    FrameLayout bottomSheetView;
    @Inject
    ReceiveMvpPresenter<AppModel, ReceiveMvpView> mReceiveMvpPresenter;
    private CommonAdapter<Wallet> mAdapter;
    private FragmentAdapter mFragmentAdapter;
    private BtcReceiveFragment mBtcReceiveFragment;
    private List<Wallet> mWalletList = new ArrayList<>();
    private Wallet mSelectedWallet;
    private int mSelectedPosition = -1;
    private BottomSheetBehavior mBottomSheetBehavior;

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

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        selectWalletView.setOnWalletClickListener(new SelectWalletView.OnWalletClickListener() {
            @Override
            public void onWalletClick(Wallet wallet, View view) {
                //弹出钱包选择界面
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }

            @Override
            public void onBackupClick(Wallet wallet, View view) {
                //跳转到备份钱包界面
                BackUpWalletActivity.start(getBaseActivity(), wallet, false);
            }
        });
        setupViewPager();

        setupBottomSheet();

    }

    private void setupBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        Decoration decor = new Decoration(getContext(), Decoration.VERTICAL);
        mRecyclerView.addItemDecoration(decor);

        mAdapter = new CommonAdapter<Wallet>(getContext(), R.layout.item_wallet_select_view, mWalletList) {

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
                        holder.setChecked(R.id.rb_selector, wallet.isSelected());
                        mSelectedWallet = wallet;
                        //刷新选择布局
                        selectWalletView.setWallet(wallet);
                        loadBtcAddress();

                    }
                    // dismiss
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });

            }
        };
        mRecyclerView.setAdapter(mAdapter);
        bottomSheetView.setOnClickListener(v -> mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
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
        if (mWalletList == null) {
            mWalletList = new ArrayList<>();
        }
        mWalletList.clear();
        mWalletList.addAll(BitbillApp.get().getWallets());
        //重置选中的钱包对象
        if (mSelectedPosition == -1 || mSelectedPosition > mWalletList.size() - 1) {
            // 选择默认的钱包对象作为选中的
            mSelectedWallet = BitbillApp.get().getDefaultWallet();
        } else {
            mSelectedWallet = mWalletList.get(mSelectedPosition);
        }
        if (selectWalletView == null) {
            return;
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
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
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
