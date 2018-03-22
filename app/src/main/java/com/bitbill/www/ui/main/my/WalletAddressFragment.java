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
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.btc.db.entity.Address;
import com.bitbill.www.model.btc.network.entity.GetTxElementResponse;
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

public class WalletAddressFragment extends BaseListFragment<AddressItem, WalletAddressMvpPresenter> implements WalletAddressMvpView, BtcAddressMvpView {

    @BindView(R.id.tv_wallet_id)
    TextView mTvWalletId;
    @BindView(R.id.tv_scan_address)
    TextView mTvScanAddress;
    @Inject
    WalletAddressMvpPresenter<BtcModel, WalletAddressMvpView> mWalletAddressMvpPresenter;
    @Inject
    BtcAddressMvpPresentder<BtcModel, BtcAddressMvpView> mBtcAddressMvpPresentder;
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
            buildData();
        }
        getMvpPresenter().requestListUnspent();
    }

    private void buildData() {
        mDatas.clear();
        for (Address address : mAddressList) {
            if (address.getIsInternal()) {
                mDatas.add((AddressItem) new AddressItem.InternalAddressItem(address.getName(), address.getBalance()).setTitle(getString(R.string.title_change_address)));
            } else {
                mDatas.add((AddressItem) new AddressItem.ExtendedAddressItem(address.getName(), address.getBalance()).setTitle(getString(R.string.title_address_balance)));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(AddressItem addressItem, int position) {
        UIHelper.copy(getBaseActivity(), addressItem.getAddress());
        showMessage(R.string.copy_address);
    }

    @Override
    protected void itemConvert(ViewHolder holder, AddressItem addressItem, int position) {   //跟前一个title不一样 显示title布局
        if (position == 0 || (StringUtils.isNotEmpty(addressItem.getTitle()) && !addressItem.getTitle().equals(mDatas.get(position - 1).getTitle()))) {
            holder.setVisible(R.id.tv_address_title, true);
            holder.setText(R.id.tv_address_title, addressItem.getTitle());
        } else {
            holder.setVisible(R.id.tv_address_title, false);
        }
        holder.setText(R.id.tv_address, addressItem.getAddress());
        Long balance = addressItem.getBalance();
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

        hideKeyboard();
    }

    @Override
    public void refreshAddressSuccess(String lastAddress, boolean isInternal) {
        hideKeyboard();
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
        hideKeyboard();

    }

    @Override
    public void loadAddressSuccess(String lastAddress) {

    }

    @Override
    public void loadAddressFail() {

    }

    @Override
    public void limitAddress(boolean limit) {

    }

    @Override
    public void getTxElementSuccess(List<GetTxElementResponse.UtxoBean> unspentList, List<GetTxElementResponse.FeesBean> fees) {
        mUnspentList = unspentList;
        if (StringUtils.isEmpty(mAddressList)) {
            return;
        }
        //重置余额
        for (Address address : mAddressList) {
            address.setBalance(0l);
        }

        if (!StringUtils.isEmpty(unspentList)) {
            for (GetTxElementResponse.UtxoBean utxoBean : mUnspentList) {
                String addressTxt = utxoBean.getAddressTxt();
                long sumOutAmount = utxoBean.getSumOutAmount();

                for (Address address : mAddressList) {
                    if (StringUtils.isNotEmpty(address.getName()) && address.getName().equals(addressTxt)) {
                        address.setBalance(address.getBalance() + sumOutAmount);
                    }
                }

            }
        }
        if (StringUtils.isEmpty(mAddressList)) {
            return;
        }
        getMvpPresenter().updateAddressBalance(mAddressList);
        buildData();

    }

    @Override
    public void getTxElementFail() {

    }

    @OnClick(R.id.tv_scan_address)
    public void onViewClicked() {
        showLoading();
        mBtcAddressMvpPresentder.refreshAddress(10, -1);
    }

}
