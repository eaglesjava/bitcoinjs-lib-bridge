package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu on 2018/1/13.
 */

public interface WalletAddressMvpView extends MvpView {
    Wallet getWallet();

    void getWalletFail();

    void getTxElementSuccess(List<GetTxElementResponse.UtxoBean> unspentList, List<GetTxElementResponse.FeesBean> fees);

    void getTxElementFail();
}
