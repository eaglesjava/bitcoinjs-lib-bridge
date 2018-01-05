package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/16.
 */
public class RefreshAddressResponse {
    private String indexNo;

    public RefreshAddressResponse(String indexNo) {
        this.indexNo = indexNo;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }
}
