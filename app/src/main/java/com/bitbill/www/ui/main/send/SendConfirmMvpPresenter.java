package com.bitbill.www.ui.main.send;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public interface SendConfirmMvpPresenter<M extends WalletModel, V extends SendConfirmMvpView> extends MvpPresenter<V> {

    /**
     * 获取utxo
     */
    void requestListUnspent();

    /**
     * 发送交易
     *
     * @param txHash 交易hash
     * @param hexTx  交易数据
     */
    void sendTransaction(String txHash, String hexTx);

    /**
     * 组装交易
     */
    void buildTransaction();

    /**
     * 估算手续费
     */
    void computeFee();
}
