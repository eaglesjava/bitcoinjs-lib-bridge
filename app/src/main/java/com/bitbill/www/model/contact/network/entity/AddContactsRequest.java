package com.bitbill.www.model.contact.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class AddContactsRequest {
    private String walletId;
    private String walletKey;
    private String address;
    private String remark;
    private String contactName;
    private String coinType;

    public AddContactsRequest(String walletId, String walletKey, String address, String remark, String contactName, String coinType) {
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

    public AddContactsRequest setWalletId(String walletId) {
        this.walletId = walletId;
        return this;
    }

    public String getWalletKey() {
        return walletKey;
    }

    public AddContactsRequest setWalletKey(String walletKey) {
        this.walletKey = walletKey;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public AddContactsRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AddContactsRequest setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getContactName() {
        return contactName;
    }

    public AddContactsRequest setContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }
}
