package com.bitbill.www.ui.main.send;

import android.content.Intent;
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
import com.bitbill.www.common.base.adapter.FragmentAdapter;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.ui.wallet.info.BchInfoFragment;
import com.bitbill.www.ui.wallet.info.EthInfoFragment;

import butterknife.BindView;

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
    private FragmentAdapter mFragmentAdapter;
    private BtcSendFragment mBtcSendFrg;

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
            //打开扫描二维码
            ScanQrcodeActivity.startForResult(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanQrcodeActivity.REQUEST_CODE_SCAN_QRCODE && resultCode == ScanQrcodeActivity.RESULT_CODE_SCAN_QRCODE_SUCCESS) {
            if (data != null) {
                String qrcodeResult = data.getStringExtra(ScanQrcodeActivity.EXTRA_SCAN_QRCODE_RESULT);
                //填充地址
                if (mBtcSendFrg != null) {
                    // TODO: 2017/12/15  校验比特币地址
                    mBtcSendFrg.setSendAddress(qrcodeResult);
                }
            }
        }
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

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        setupViewPager();
    }

    private void setupViewPager() {
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        mBtcSendFrg = BtcSendFragment.newInstance();
        mFragmentAdapter.addItem("btc", mBtcSendFrg);
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
        return R.layout.fragment_send;
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {

    }

    public void sendSuccess() {
        //reset ui
        if (mBtcSendFrg != null) {
            mBtcSendFrg.sendSuccess();
        }
    }
}
