package com.bitbill.www.model.entity.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/12/5.
 */
public class WalletBackupSuccess extends MessageEvent {
    private Wallet mWallet;

    public WalletBackupSuccess(Wallet wallet) {
        mWallet = wallet;
    }

    public Wallet getWallet() {
        return mWallet;
    }
}
