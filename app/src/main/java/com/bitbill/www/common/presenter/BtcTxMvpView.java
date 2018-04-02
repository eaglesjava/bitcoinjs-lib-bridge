package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface BtcTxMvpView extends MvpView {

    void getWalletFail(Long walletId);

    void getTxRecordSuccess(List<TxElement> list, Long walletId);

    void getTxRecordFail(Long walletId);

    void listUnconfirmSuccess(List<TxElement> data);

    void listUnconfirmFail();


}
