package com.bitbill.www.ui.main.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.common.presenter.WalletMvpPresenter;
import com.bitbill.www.common.presenter.WalletMvpView;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletSettingFragment extends BaseListFragment<Wallet, WalletMvpPresenter> implements WalletMvpView {

    @Inject
    WalletMvpPresenter<WalletModel, WalletMvpView> mWalletMvpPresenter;

    public WalletSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WalletSettingFragment.
     */
    public static WalletSettingFragment newInstance() {
        WalletSettingFragment fragment = new WalletSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public WalletMvpPresenter getMvpPresenter() {
        return mWalletMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }


    @Override
    public void initData() {
        getMvpPresenter().loadWallets();
    }

    @Override
    protected void onListItemClick(Wallet wallet, int position) {
        // 跳转到钱包详情界面
        WalletDetailActivity.start(getBaseActivity(), wallet);
    }

    @Override
    protected void itemConvert(ViewHolder holder, Wallet wallet, int position) {
        holder.setText(R.id.tv_wallet_name, String.format(getString(R.string.text_someone_wallet), wallet.getName()));
        holder.setVisible(R.id.tv_back_up, !wallet.getIsBackuped());
        holder.setOnClickListener(R.id.tv_back_up, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到备份功能
                BackUpWalletActivity.start(getBaseActivity(), wallet, true);
            }
        });

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_wallet_setting;
    }

    @Override
    public void loadWalletsSuccess(List<Wallet> wallets) {
        //设置全局钱包列表对象
        updateWallets(wallets);
    }

    private void updateWallets(List<Wallet> wallets) {
        BitbillApp.get().setWallets(wallets);
        setDatas(wallets);
    }

    @Override
    public void loadWalletsFail() {
        showMessage("加载钱包信息失败，请返回重试");
    }

    public void updateWallet(Wallet wallet) {
        if (isDataEmpty()) {
            return;
        }
        int position = mDatas.indexOf(wallet);
        if (position != -1) {
            mDatas.get(position).setIsBackuped(wallet.getIsBackuped());
        }
        notifyItemChanged(position);
    }

    public void deleteWallet(Wallet wallet) {
        //移除全局钱包对象
        BitbillApp.get().getWallets().remove(wallet);
        if (isDataEmpty()) {
            return;
        }

        notifyItemRemoved(mDatas.indexOf(wallet));
    }

}
