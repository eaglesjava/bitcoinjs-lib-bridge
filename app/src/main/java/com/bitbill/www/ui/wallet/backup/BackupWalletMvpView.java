package com.bitbill.www.ui.wallet.backup;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/11/23.
 */
public interface BackupWalletMvpView extends MvpView {

    Wallet getWallet();

    void loadMnemonicSuccess(String mnemonic);

    void loadMnemonicFail();

    void getWalletFail();

    void getConfirmPwdFail();

}
