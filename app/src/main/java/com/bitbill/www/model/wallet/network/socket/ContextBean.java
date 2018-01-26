package com.bitbill.www.model.wallet.network.socket;

public class ContextBean {
    /**
     * walletId : bitpie
     * amount : 0.01
     * indexNo : 132
     * changeIndexNo : 114
     * type : RECEIVE
     * txHash : 7415b5b0c12ef149539ee90c6bb751d808dc3767a3599a19aad0dc92fda77cea
     * version : 37
     * height : 506045
     */

    private String walletId;
    private String amount;
    private long indexNo;
    private long changeIndexNo;
    private String type;
    private String txHash;
    private long version;
    private long height;

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

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }
}