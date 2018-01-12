package com.bitbill.www.ui.wallet.info;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.transaction.TxModel;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface BtcRecordMvpPresenter<M extends TxModel, V extends BtcRecordMvpView> extends MvpPresenter<V> {

    void loadTxRecord();

    void requestTxRecord();
}
