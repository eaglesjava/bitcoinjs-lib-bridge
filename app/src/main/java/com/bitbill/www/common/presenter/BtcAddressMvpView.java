package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface BtcAddressMvpView extends MvpView {

    Wallet getWallet();

    void getWalletFail();

    void refreshAddressFail(boolean isInternal, boolean silence);

    void refreshAddressSuccess(String lastAddress, boolean isInternal, boolean silence);

    void reachAddressIndexLimit(boolean silence);

    void loadAddressSuccess(String lastAddress);

    void loadAddressFail();

}
