package com.bitbill.www.model.eventbus;

import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/28.
 */

public class WalletDeleteEvent {

    private Wallet mWallet;

    public WalletDeleteEvent() {
    }

    public WalletDeleteEvent(Wallet wallet) {
        mWallet = wallet;
    }

    public Wallet getWallet() {
        return mWallet;
    }

    public void setWallet(Wallet wallet) {
        mWallet = wallet;
    }
}
