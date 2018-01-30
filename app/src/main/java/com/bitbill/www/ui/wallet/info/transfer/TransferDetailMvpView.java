package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.transaction.db.entity.TxRecord;

/**
 * Created by isanwenyu on 2018/1/30.
 */

public interface TransferDetailMvpView extends MvpView {
    TxRecord getTxRecord();

    void buildDataSuccess(TxRecord txRecord);

    void buildDataFail();
}
