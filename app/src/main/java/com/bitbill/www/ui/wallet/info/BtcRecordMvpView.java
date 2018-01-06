package com.bitbill.www.ui.wallet.info;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.TxElement;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface BtcRecordMvpView extends MvpView {
    Wallet getWallet();

    void getWalletFail();

    void getTxRecordSuccess(List<TxElement> list);

    void getTxRecordFail();
}
