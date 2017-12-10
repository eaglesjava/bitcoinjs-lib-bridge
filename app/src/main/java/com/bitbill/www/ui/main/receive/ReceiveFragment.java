package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.base.view.dialog.MessageConfirmDialog;
import com.bitbill.www.common.base.view.widget.WalletView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    @BindView(R.id.wv_select)
    WalletView selectWalletView;
    private FragmentAdapter mFragmentAdapter;
    private BtcReceiveFragment mBtcReceiveFragment;
    private List<Wallet> mWalletList;
    private WalletSelectDialog mWalletSelectDialog;
    private Wallet mSelectedWallet;

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
        setHasOptionsMenu(true);
        mWalletList = new ArrayList<>();

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        setupViewPager();

        mWalletSelectDialog = WalletSelectDialog.newInstance();
        mWalletSelectDialog.setOnWalletSelectItemClickListener(new WalletSelectDialog.OnWalletSelectItemClickListener() {
            @Override
            public void onItemSelected(Wallet selectedWallet, int position) {
                mSelectedWallet = selectedWallet;
                //刷新选择布局
                selectWalletView.setWallet(selectedWallet);
            }
        });

        selectWalletView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出钱包选择界面
                mWalletSelectDialog.show(getChildFragmentManager(), WalletSelectDialog.TAG);
            }
        });
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

            mWalletList = BitbillApp.get().getWallets();
            // 选择默认的钱包对象作为选中的
            mSelectedWallet = getDefaultWallet();
            if (mSelectedWallet != null) {
                mSelectedWallet.setSelected(true);
                selectWalletView.setWallet(mSelectedWallet);
            } else {
                selectWalletView.setVisibility(View.GONE);
            }
            MessageConfirmDialog.newInstance("友情提醒", "为保护您的隐私，每次转入操作时，都将使用新地址，已使用的旧地址仍然可用", false)
                    .show(getChildFragmentManager(), MessageConfirmDialog.TAG);
        }


    }

    @Nullable
    private Wallet getDefaultWallet() {
        if (StringUtils.isEmpty(mWalletList)) return null;
        Wallet defaultWallet = null;
        for (Wallet wallet : mWalletList) {
            if (wallet.isDefault()) {
                defaultWallet = wallet;
                return defaultWallet;
            }
        }
        return defaultWallet;
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
            // TODO: 2017/12/8 刷新接收地址
            showMessage("刷新地址");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Wallet getSelectedWallet() {
        return mSelectedWallet;
    }
}
