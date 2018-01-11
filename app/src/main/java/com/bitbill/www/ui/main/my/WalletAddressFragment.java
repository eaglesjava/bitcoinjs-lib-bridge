package com.bitbill.www.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.common.presenter.BtcAddressMvpPresentder;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class WalletAddressFragment extends BaseListFragment<Address, BtcAddressMvpPresentder> {

    @BindView(R.id.tv_wallet_id)
    TextView mTvWalletId;
    @BindView(R.id.tv_scan_address)
    TextView mTvScanAddress;
    private Wallet mWallet;
    private List<Address> mAddressList;

    public static WalletAddressFragment newInstance(Wallet wallet) {
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_WALLET, wallet);
        WalletAddressFragment fragment = new WalletAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BtcAddressMvpPresentder getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void initData() {
        mWallet = (Wallet) getArguments().getSerializable(AppConstants.ARG_WALLET);
        if (mWallet != null) {
            mWallet.__setDaoSession(getApp().getDaoSession());
            String name = mWallet.getName();
            mTvWalletId.setText(name);
            mAddressList = mWallet.getAddressList();
            setDatas(mAddressList);
        }

    }

    @Override
    protected void onListItemClick(Address address, int position) {
        UIHelper.copy(getBaseActivity(), address.getName());
        showMessage(R.string.copy_address);
    }

    @Override
    protected void itemConvert(ViewHolder holder, Address address, int position) {
        holder.setText(R.id.tv_address, address.getName());
        holder.setText(R.id.tv_amount, StringUtils.satoshi2btc(address.getBalance()) + " BTC");


    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_wallet_address;
    }

    @NonNull
    @Override
    public DividerDecoration getDecoration() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wallet_address;
    }

}
