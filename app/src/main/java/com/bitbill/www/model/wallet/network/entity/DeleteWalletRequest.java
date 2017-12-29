package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class DeleteWalletRequest {
    private String extendedKeysHash;
    private String clientId;

    public DeleteWalletRequest(String extendedKeysHash, String clientId) {
        this.extendedKeysHash = extendedKeysHash;
        this.clientId = clientId;
    }
}
