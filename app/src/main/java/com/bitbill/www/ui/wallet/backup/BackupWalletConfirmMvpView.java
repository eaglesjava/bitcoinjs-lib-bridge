package com.bitbill.www.ui.wallet.backup;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public interface BackupWalletConfirmMvpView extends MvpView {
    void backupSuccess();

    void backupFail();

    String[] getMnemonicArray();

    List<String> getMnemonicConfirmList();

    Wallet getWallet();

}
