package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
public class GetLastAddressRequest {
    private String walletId;

    public GetLastAddressRequest(String walletId) {
        this.walletId = walletId;
    }
}
