package com.bitbill.www.model.address.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/16.
 */
public class RefreshAddressResponse {
    private long indexNo;
    private long changeIndexNo;

    public long getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(long indexNo) {
        this.indexNo = indexNo;
    }

    public long getChangeIndexNo() {
        return changeIndexNo;
    }

    public void setChangeIndexNo(long changeIndexNo) {
        this.changeIndexNo = changeIndexNo;
    }
}
