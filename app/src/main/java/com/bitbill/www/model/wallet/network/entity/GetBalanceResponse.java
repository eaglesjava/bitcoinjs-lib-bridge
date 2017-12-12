package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public class GetBalanceResponse {


    /**
     * balance	long	钱包余额（单位：satoshi）
     * unconfirm	long	未确认金额（单位：satoshi）
     */

    private long balance;
    private long unconfirm;

    public double getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getUnconfirm() {
        return unconfirm;
    }

    public GetBalanceResponse setUnconfirm(long unconfirm) {
        this.unconfirm = unconfirm;
        return this;
    }
}
