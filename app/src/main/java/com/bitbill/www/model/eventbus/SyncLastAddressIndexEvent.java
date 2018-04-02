package com.bitbill.www.model.eventbus;

import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2018/4/1.
 */
public class SyncLastAddressIndexEvent {

    private long mIndexNo;
    private long mChangeIndexNo;
    private Wallet mWallet;

    public SyncLastAddressIndexEvent(long indexNo, long changeIndexNo, Wallet wallet) {
        mIndexNo = indexNo;
        mChangeIndexNo = changeIndexNo;
        mWallet = wallet;
    }

    public long getIndexNo() {
        return mIndexNo;
    }

    public SyncLastAddressIndexEvent setIndexNo(long indexNo) {
        mIndexNo = indexNo;
        return this;
    }

    public long getChangeIndexNo() {
        return mChangeIndexNo;
    }

    public SyncLastAddressIndexEvent setChangeIndexNo(long changeIndexNo) {
        mChangeIndexNo = changeIndexNo;
        return this;
    }

    public Wallet getWallet() {
        return mWallet;
    }

    public SyncLastAddressIndexEvent setWallet(Wallet wallet) {
        mWallet = wallet;
        return this;
    }
}
