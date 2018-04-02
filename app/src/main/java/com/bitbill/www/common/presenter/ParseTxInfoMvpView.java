package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.transaction.db.entity.TxRecord;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface ParseTxInfoMvpView extends MvpView {

    void getTxInfoListFail(Long walletId, String TAG);

    void parsedTxItemList(List<TxRecord> txRecords, Long walletId, String TAG);

    void parsedTxItemListFail(Long walletId, String TAG);
}
