package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/12/5.
 */
public class WalletUpdateEvent extends MessageEvent {

    private Wallet mWallet;

    public WalletUpdateEvent(Wallet wallet) {
        mWallet = wallet;
    }

    public WalletUpdateEvent() {

    }

    public Wallet getWallet() {
        return mWallet;
    }

    public void setWallet(Wallet wallet) {
        mWallet = wallet;
    }
}
