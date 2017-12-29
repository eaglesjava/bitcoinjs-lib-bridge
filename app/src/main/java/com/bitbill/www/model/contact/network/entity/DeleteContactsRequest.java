package com.bitbill.www.model.contact.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class DeleteContactsRequest {

    private String walletId;
    private String walletKey;
    private String address;

    public DeleteContactsRequest(String walletId, String walletKey, String address) {
        this.walletId = walletId;
        this.walletKey = walletKey;
        this.address = address;
    }
}
