package com.bitbill.www.ui.main.send;

import android.os.Bundle;
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
import com.bitbill.www.common.base.view.widget.SelectWalletView;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.receive.WalletSelectDialog;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFragment extends BaseLazyFragment {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.wv_select)
    SelectWalletView selectWalletView;
    private FragmentAdapter mFragmentAdapter;
    private BtcSendFragment mBtcSendFrg;
    private List<Wallet> mWalletList;
    private Wallet mSelectedWallet;
    private WalletSelectDialog mWalletSelectDialog;

    public SendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SendFragment.
     */
    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.send_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_scan) {
            // TODO: 2017/12/8 刷新接收地址
            showMessage("扫描二维码");
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    }

    private void setupViewPager() {
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        mBtcSendFrg = BtcSendFragment.newInstance();
        mFragmentAdapter.addItem("btc", mBtcSendFrg);
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
        return R.layout.fragment_send;
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {

    }

    @OnClick(value = R.id.btn_send)
    public void sendClick(View v) {
        // TODO: 2017/12/9 调用发送逻辑 成功跳转到发送成功界面 否则报错
        SendSuccessActivity.start(getBaseActivity());


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            mWalletList.clear();
            mWalletList.addAll(BitbillApp.get().getWallets());
            // 选择默认的钱包对象作为选中的
            mSelectedWallet = BitbillApp.get().getDefaultWallet();
            if (mSelectedWallet != null) {
                //重置单选select对象
                for (Wallet wallet : mWalletList) {
                    if (wallet.equals(mSelectedWallet)) {
                        wallet.setSelected(true);
                    } else {
                        wallet.setSelected(false);
                    }
                }
                mSelectedWallet.setSelected(true);
                selectWalletView.setWallet(mSelectedWallet);
            } else {
                selectWalletView.setVisibility(View.GONE);
            }
        }


    }
}
