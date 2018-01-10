package com.bitbill.www.ui.main.send;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.transaction.TxModel;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public interface SendConfirmMvpPresenter<M extends TxModel, V extends SendConfirmMvpView> extends MvpPresenter<V> {

    /**
     * 获取utxo
     */
    void requestListUnspent();

    /**
     * 发送交易
     *
     * @param hash
     * @param txHex
     */
    void sendTransaction(String hash, String txHex, String inaddress, String outaddress);

    /**
     * 组装交易
     */
    void buildTransaction();

    /**
     * 估算手续费
     */
    void computeFee();
}
