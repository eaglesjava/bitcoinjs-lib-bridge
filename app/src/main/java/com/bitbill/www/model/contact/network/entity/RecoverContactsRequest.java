package com.bitbill.www.model.contact.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class RecoverContactsRequest {
    private String walletKey;
    private String recoverKey;

    public RecoverContactsRequest(String walletKey, String recoverKey) {
        this.walletKey = walletKey;
        this.recoverKey = recoverKey;
    }
}
