package com.bitbill.www.model.contact.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class UpdateContactsRequest {

    private String walletId;
    private String walletKey;
    private String address;
    private String remark;
    private String contactName;
    private String coinType;

    public UpdateContactsRequest(String walletId, String walletKey, String address, String remark, String contactName, String coinType) {
        this.walletId = walletId;
        this.walletKey = walletKey;
        this.address = address;
        this.remark = remark;
        this.contactName = contactName;
        this.coinType = coinType;
    }

    public String getWalletId() {
        return walletId;
    }

    public UpdateContactsRequest setWalletId(String walletId) {
        this.walletId = walletId;
        return this;
    }

    public String getWalletKey() {
        return walletKey;
    }

    public UpdateContactsRequest setWalletKey(String walletKey) {
        this.walletKey = walletKey;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UpdateContactsRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public UpdateContactsRequest setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getContactName() {
        return contactName;
    }

    public UpdateContactsRequest setContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }
}
