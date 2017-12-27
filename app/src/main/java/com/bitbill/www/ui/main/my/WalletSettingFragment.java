package com.bitbill.www.ui.main.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletSettingFragment extends BaseListFragment<Wallet, MvpPresenter> {


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
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }


    @Override
    public void initData() {
        setDatas(BitbillApp.get().getWallets());
    }

    @Override
    protected void onListItemClick(Wallet wallet, int position) {
        // TODO: 2017/12/27 跳转到钱包详情界面
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

}
