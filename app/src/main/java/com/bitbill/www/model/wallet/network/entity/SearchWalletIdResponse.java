package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class SearchWalletIdResponse {
    private String address;

    public SearchWalletIdResponse(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public SearchWalletIdResponse setAddress(String address) {
        this.address = address;
        return this;
    }
}
