package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public class GetWalletIdRequest {
    private String extendedKeysHash;//扩展公钥MD5

    public GetWalletIdRequest(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }

    public String getExtendedKeysHash() {
        return extendedKeysHash;
    }

    public GetWalletIdRequest setExtendedKeysHash(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
        return this;
    }
}
