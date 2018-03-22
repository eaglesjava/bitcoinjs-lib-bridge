package com.bitbill.www.model.btc.network.entity;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public class GetTxElement {
    /**
     * 扩展公钥MD5
     */
    private String extendedKeysHash;

    public GetTxElement(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }

    public String getExtendedKeysHash() {
        return extendedKeysHash;
    }

    public void setExtendedKeysHash(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }
}
