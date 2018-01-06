package com.bitbill.www.ui.wallet.info;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface BtcRecordMvpPresenter<M extends WalletModel, V extends BtcRecordMvpView> extends MvpPresenter<V> {
    void getTxRecord();
}
