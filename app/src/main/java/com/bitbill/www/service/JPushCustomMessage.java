package com.bitbill.www.service;

import com.bitbill.www.common.base.model.entity.Entity;

/**
 * Created by isanwenyu on 2018/1/26.
 */

public class JPushCustomMessage extends Entity {

    /**
     * walletId : localTest
     * amount : 1000
     * indexNo : 10
     * changeIndexNo : 10
     * type : RECEIVE
     * txHash : c0ccbff6855c5631d30889f95b4b751667255fdfe14a70ae7a1418bb9ac8c1b3
     * version : 0
     */

    private String walletId;
    private String amount;
    private long indexNo;
    private long changeIndexNo;
    private String type;
    private String txHash;
    private long version;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(long indexNo) {
        this.indexNo = indexNo;
    }

    public long getChangeIndexNo() {
        return changeIndexNo;
    }

    public void setChangeIndexNo(long changeIndexNo) {
        this.changeIndexNo = changeIndexNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
