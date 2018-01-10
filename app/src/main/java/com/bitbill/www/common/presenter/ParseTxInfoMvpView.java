package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface ParseTxInfoMvpView extends MvpView {
    List<TxElement> getTxInfoList();

    void requireTxInfoList();

    void getTxInfoListFail();

    void parsedTxItemList(List<TxRecord> txRecords);

    void parsedTxItemListFail();
}
