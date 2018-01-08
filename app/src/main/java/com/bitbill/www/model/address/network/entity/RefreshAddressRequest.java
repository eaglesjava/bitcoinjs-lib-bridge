package com.bitbill.www.model.address.network.entity;

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

    public RefreshAddressRequest(String extendedKeysHash, long indexNo) {
        this.extendedKeysHash = extendedKeysHash;
        this.indexNo = indexNo;
    }

    public String getExtendedKeysHash() {
        return extendedKeysHash;
    }

    public RefreshAddressRequest setExtendedKeysHash(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
        return this;
    }
}
