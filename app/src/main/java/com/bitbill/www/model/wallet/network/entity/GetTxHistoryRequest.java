package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
public class GetTxHistoryRequest {

    private String extendedKeysHash;

    public GetTxHistoryRequest(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }
}
