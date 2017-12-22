package com.bitbill.www.ui.main.send;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetTxElementResponse;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public interface SendConfirmMvpView extends MvpView {
    Wallet getWallet();

    void sendTransactionFail(String message);

    void sendTransactionSuccess();

    boolean isSendAll();

    long getFeeByte();

    long getSendAmount();

    String getSendAddress();

    String getTradePwd();

    void requireTradePwd();

    void invalidTradePwd();

    void getWalletFail();

    void getTxElementFail();

    void getTxElementSuccess(List<GetTxElementResponse.UtxoBean> unspentList, List<GetTxElementResponse.FeesBean> fees);


    String getNewAddress();

    List<GetTxElementResponse.UtxoBean> getUnspentList();

    void compteFee(long fee);

    /**
     * 余额不足
     */
    void amountNoEnough();

    String getRemark();
}
