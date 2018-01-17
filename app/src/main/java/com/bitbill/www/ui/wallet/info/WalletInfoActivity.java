package com.bitbill.www.ui.wallet.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseTabsActivity;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.info.transfer.TransferDetailsActivity;

public class WalletInfoActivity extends BaseTabsActivity implements BtcRecordFragment.OnTransactionRecordItemClickListener {

    private Wallet mWallet;

    public static void start(Context context, Wallet wallet) {
        Intent intent = new Intent(context, WalletInfoActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
    }

    @Override
    public void OnTransactionRecordItemClick(TxRecord item) {
        //跳转到转账详情页面
        TransferDetailsActivity.start(WalletInfoActivity.this, item);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) intent.getSerializableExtra(AppConstants.EXTRA_WALLET);
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
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isBlue() {
        return true;
    }

    @Override
    protected BaseFragment getBtcFragment() {
        return BtcRecordFragment.newInstance(mWallet);
    }


    @Override
    public void initData() {
        setTitle(mWallet.getName() + " 的钱包");
    }

}
