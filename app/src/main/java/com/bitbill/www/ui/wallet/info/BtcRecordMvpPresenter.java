package com.bitbill.www.ui.wallet.info;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface BtcRecordMvpPresenter<M extends BtcModel, V extends BtcRecordMvpView> extends MvpPresenter<V> {

    void loadTxRecord(Wallet wallet);

    void requestTxRecord(Wallet wallet);
}
