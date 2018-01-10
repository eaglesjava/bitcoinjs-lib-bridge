package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.transaction.TxModel;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface ParseTxInfoMvpPresenter<M extends TxModel, V extends ParseTxInfoMvpView> extends MvpPresenter<V> {

    void parseTxInfo();
}
