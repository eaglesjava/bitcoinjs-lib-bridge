package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class GetLastAddressResponse {

    private String address;

    public GetLastAddressResponse(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public GetLastAddressResponse setAddress(String address) {
        this.address = address;
        return this;
    }
}
