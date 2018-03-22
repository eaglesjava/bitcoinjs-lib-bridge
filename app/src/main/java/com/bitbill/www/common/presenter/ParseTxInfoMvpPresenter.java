package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.btc.network.entity.TxElement;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface ParseTxInfoMvpPresenter<M extends BtcModel, V extends ParseTxInfoMvpView> extends MvpPresenter<V> {

    void parseTxInfo(List<TxElement> list);
}
