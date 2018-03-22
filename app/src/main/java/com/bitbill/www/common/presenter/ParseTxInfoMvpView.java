package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.btc.db.entity.TxRecord;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface ParseTxInfoMvpView extends MvpView {

    void requireTxInfoList();

    void getTxInfoListFail();

    void parsedTxItemList(List<TxRecord> txRecords);

    void parsedTxItemListFail();
}
