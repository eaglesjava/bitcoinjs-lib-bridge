package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface BtcTxMvpPresenter<M extends TxModel, V extends BtcTxMvpView> extends MvpPresenter<V> {

    void requestTxRecord(Wallet wallet);

    void listUnconfirm();
}
