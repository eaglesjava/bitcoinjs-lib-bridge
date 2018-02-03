package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface BtcAddressMvpView extends MvpView {

    Wallet getWallet();

    void getWalletFail();

    void refreshAddressFail(boolean isInternal);

    void refreshAddressSuccess(String lastAddress, boolean isInternal);

    void reachAddressIndexLimit();

    void loadAddressSuccess(String lastAddress);

    void loadAddressFail();

    void limitAddress(boolean limit);
}
