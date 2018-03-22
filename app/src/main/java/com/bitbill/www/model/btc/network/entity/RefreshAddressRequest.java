package com.bitbill.www.model.btc.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
public class RefreshAddressRequest {
    /**
     * extendedKeysHash	true	String	扩展公钥MD5
     * indexNo	true	Long	地址索引(当前地址索引)
     */
    private String extendedKeysHash;
    private long indexNo;
    private long changeIndexNo;

    public RefreshAddressRequest(String extendedKeysHash, long indexNo, long changeIndexNo) {
        this.extendedKeysHash = extendedKeysHash;
        this.indexNo = indexNo;
        this.changeIndexNo = changeIndexNo;
    }

    public String getExtendedKeysHash() {
        return extendedKeysHash;
    }

    public RefreshAddressRequest setExtendedKeysHash(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
        return this;
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
}
