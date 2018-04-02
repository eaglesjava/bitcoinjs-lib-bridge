package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2018/2/4.
 */
public class SyncAddressEvent extends MessageEvent {
    private Wallet mWallet;

    public SyncAddressEvent(Wallet wallet) {
        mWallet = wallet;
    }

    public Wallet getWallet() {
        return mWallet;
    }

    public SyncAddressEvent setWallet(Wallet wallet) {
        mWallet = wallet;
        return this;
    }
}
