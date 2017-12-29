package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class GetCacheVersionRequest {
    private String extendedKeysHash;

    public GetCacheVersionRequest(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }
}
