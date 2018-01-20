package com.bitbill.www.ui.wallet.info.transfer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseFragmentActivity;
import com.bitbill.www.model.transaction.db.entity.TxRecord;

public class TransferDetailsActivity extends BaseFragmentActivity {

    private TxRecord mTxRecord;

    public static void start(Context context, TxRecord txRecord) {
        Intent starter = new Intent(context, TransferDetailsActivity.class);
        starter.putExtra(AppConstants.EXTRA_TX_ITEM, txRecord);
        context.startActivity(starter);
    }

    @Override
    public void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        setTitle(R.string.title_activity_transfer_details);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mTxRecord = ((TxRecord) getIntent().getSerializableExtra(AppConstants.EXTRA_TX_ITEM));
    }

    @Override
    protected Fragment getFragment() {
        return TransferDetailFragment.newInstance(mTxRecord);
    }
}
