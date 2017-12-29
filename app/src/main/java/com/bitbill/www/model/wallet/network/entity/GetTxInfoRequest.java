package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class GetTxInfoRequest {
    private String txHash;

    public GetTxInfoRequest(String txHash) {
        this.txHash = txHash;
    }
}
