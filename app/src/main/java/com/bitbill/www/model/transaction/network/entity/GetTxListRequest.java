package com.bitbill.www.model.transaction.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
public class GetTxListRequest {

    private String extendedKeysHash;

    private long id;

    public GetTxListRequest(String extendedKeysHash, long id) {
        this.extendedKeysHash = extendedKeysHash;
        this.id = id;
    }
}
