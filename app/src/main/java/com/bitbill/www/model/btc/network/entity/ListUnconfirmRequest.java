package com.bitbill.www.model.btc.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
public class ListUnconfirmRequest {
    private String extendedKeysHash;

    public ListUnconfirmRequest(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }
}
