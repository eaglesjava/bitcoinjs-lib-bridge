package com.bitbill.www.model.eventbus;

import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2018/4/1.
 */
public class RequestRecordEvent {

    private Wallet mWallet;

    public RequestRecordEvent(Wallet wallet) {
        mWallet = wallet;
    }

    public Wallet getWallet() {
        return mWallet;
    }

    public void setWallet(Wallet wallet) {
        mWallet = wallet;
    }
}
