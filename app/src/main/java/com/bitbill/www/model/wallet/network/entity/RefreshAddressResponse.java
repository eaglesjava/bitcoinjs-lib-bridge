package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/16.
 */
public class RefreshAddressResponse {
    private long indexNo;

    public RefreshAddressResponse(long indexNo) {
        this.indexNo = indexNo;
    }

    public long getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(long indexNo) {
        this.indexNo = indexNo;
    }
}
