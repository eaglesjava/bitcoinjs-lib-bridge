package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiveFragment extends BaseLazyFragment {

    private static final String TAG = "ReceiveFragment";
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private FragmentAdapter mFragmentAdapter;
    private BtcReceiveFragment mBtcReceiveFragment;
    private BottomSheetBehavior<View> mBehavior;
    private List<Wallet> mWalletList;
    private CommonAdapter<Wallet> mAdapter;

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
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void onBeforeSetContentLayout() {
        mWalletList = new ArrayList<>();

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        setupViewPager();
        initBottomSheetView();
    }

    private void initBottomSheetView() {

        mBehavior = BottomSheetBehavior.from(mRecyclerView);

        mAdapter = new CommonAdapter<Wallet>(getBaseActivity(), R.layout.item_wallet_select_view, mWalletList) {

            @Override
            protected void convert(ViewHolder holder, Wallet wallet, int position) {
                holder.setText(R.id.tv_wallet_name, wallet.getName() + " 的钱包");
                holder.setText(R.id.tv_wallet_amount, StringUtils.formatBtcAmount(wallet.getBtcAmount()) + " btc");
                holder.setText(R.id.tv_wallet_label, String.valueOf(wallet.getName().charAt(0)));
                holder.setOnClickListener(R.id.item_wallet_select_container, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupViewPager() {
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        mBtcReceiveFragment = BtcReceiveFragment.newInstance();
        mFragmentAdapter.addItem("btc", mBtcReceiveFragment);
        mFragmentAdapter.addItem("eth", EthInfoFragment.newInstance());
        mFragmentAdapter.addItem("bch", BchInfoFragment.newInstance());
        viewPager.setAdapter(mFragmentAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_receive;
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            List<Wallet> wallets = BitbillApp.get().getWallets();
            if (StringUtils.isEmpty(wallets)) return;
            mWalletList.clear();
            mWalletList.addAll(wallets);
            mAdapter.notifyDataSetChanged();
        }
    }

}
