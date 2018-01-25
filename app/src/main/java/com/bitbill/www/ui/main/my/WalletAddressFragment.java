package com.bitbill.www.ui.main.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.common.presenter.BtcAddressMvpPresentder;
import com.bitbill.www.common.presenter.BtcAddressMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class WalletAddressFragment extends BaseListFragment<Address, WalletAddressMvpPresenter> implements WalletAddressMvpView, BtcAddressMvpView {

    @BindView(R.id.tv_wallet_id)
    TextView mTvWalletId;
    @BindView(R.id.tv_scan_address)
    TextView mTvScanAddress;
    @Inject
    WalletAddressMvpPresenter<TxModel, WalletAddressMvpView> mWalletAddressMvpPresenter;
    @Inject
    BtcAddressMvpPresentder<AddressModel, BtcAddressMvpView> mBtcAddressMvpPresentder;
    private Wallet mWallet;
    private List<Address> mAddressList;
    private List<GetTxElementResponse.UtxoBean> mUnspentList;

    public static WalletAddressFragment newInstance(Wallet wallet) {
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_WALLET, wallet);
        WalletAddressFragment fragment = new WalletAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public WalletAddressMvpPresenter getMvpPresenter() {
        return mWalletAddressMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
        addPresenter(mBtcAddressMvpPresentder);
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
        getMvpPresenter().requestListUnspent();
    }

    @Override
    protected void onListItemClick(Address address, int position) {
        UIHelper.copy(getBaseActivity(), address.getName());
        showMessage(R.string.copy_address);
    }

    @Override
    protected void itemConvert(ViewHolder holder, Address address, int position) {
        holder.setText(R.id.tv_address, address.getName());
        Long balance = address.getBalance();
        holder.setText(R.id.tv_amount, StringUtils.satoshi2btc(balance) + " BTC");
        holder.setAlpha(R.id.tv_amount, balance > 0 ? 1.0f : 0.6f);


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

    @Override
    public Wallet getWallet() {
        return mWallet;
    }

    @Override
    public void getWalletFail() {
        showMessage(R.string.msg_get_wallet_info_fail);
    }

    @Override
    public void refreshAddressFail(boolean isInternal) {

    }

    @Override
    public void refreshAddressSuccess(String lastAddress, boolean isInternal) {
        mWallet.resetAddressList();
        if (mAddressList == null) {
            mAddressList = new ArrayList<>();
        }
        mAddressList.clear();
        mAddressList.addAll(mWallet.getAddressList());
        getMvpPresenter().requestListUnspent();
        showMessage(R.string.successs_address_refresh);
    }

    @Override
    public void reachAddressIndexLimit() {

        showMessage(R.string.fail_reach_address_index_limit);

    }

    @Override
    public void loadAddressSuccess(String lastAddress) {

    }

    @Override
    public void loadAddressFail() {

    }

    @Override
    public void getTxElementSuccess(List<GetTxElementResponse.UtxoBean> unspentList, List<GetTxElementResponse.FeesBean> fees) {
        mUnspentList = unspentList;
        for (GetTxElementResponse.UtxoBean utxoBean : mUnspentList) {
            long addressIndex = utxoBean.getAddressIndex();
            long sumOutAmount = utxoBean.getSumOutAmount();
            for (Address address : mAddressList) {
                if (address.getIndex() == addressIndex) {
                    address.setBalance(sumOutAmount);
                }
            }

        }
        setDatas(mAddressList);
        getMvpPresenter().updateAddressBalance(mAddressList);

    }

    @Override
    public void amountNoEnough() {

    }

    @Override
    public void getTxElementFail() {

    }

    @OnClick(R.id.tv_scan_address)
    public void onViewClicked() {
        mBtcAddressMvpPresentder.refreshAddress(10, -1);
    }
}
