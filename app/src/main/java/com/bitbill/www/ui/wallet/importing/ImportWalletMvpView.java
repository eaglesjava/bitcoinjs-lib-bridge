package com.bitbill.www.ui.wallet.importing;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
public interface ImportWalletMvpView extends MvpView {

    void importWalletSuccess();

    void importWalletFail();

    Wallet getWallet();

    String getMnemonic();

    void getWalletInfoFail();

    void getMnemonicFail();

    void inputMnemonicError();
}
