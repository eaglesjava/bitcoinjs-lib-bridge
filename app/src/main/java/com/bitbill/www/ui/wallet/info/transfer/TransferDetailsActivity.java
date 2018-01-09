package com.bitbill.www.ui.wallet.info.transfer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseFragmentActivity;
import com.bitbill.www.model.wallet.network.entity.TxItem;

public class TransferDetailsActivity extends BaseFragmentActivity {

    private TxItem mTxItem;

    public static void start(Context context, TxItem txItem) {
        Intent starter = new Intent(context, TransferDetailsActivity.class);
        starter.putExtra(AppConstants.EXTRA_TX_ITEM, txItem);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mTxItem = ((TxItem) getIntent().getSerializableExtra(AppConstants.EXTRA_TX_ITEM));
    }

    @Override
    protected Fragment getFragment() {
        return TransferDetailFragment.newInstance(mTxItem);
    }
}
