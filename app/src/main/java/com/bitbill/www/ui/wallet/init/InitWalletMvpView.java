package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
public interface InitWalletMvpView extends MvpView {

    void createWalletSuccess();

    void createWalletFail();

    String getWalletId();

    String getConfirmTradePwd();

    String getTradePwd();

    void requireTradeConfirmPwd();

    void isPwdInConsistent();

    void requireTradePwd();

    void invalidTradePwd();

    Wallet getWallet();

    boolean isCreateWallet();

    void initWalletInfoFail();

    void invalidWalletId();

    boolean isResetPwd();
}
