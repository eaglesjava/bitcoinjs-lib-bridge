package com.bitbill.www.model.contact.network.entity;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class RecoverContactsResponse {


    private List<ContactsBean> contacts;

    public List<ContactsBean> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsBean> contacts) {
        this.contacts = contacts;
    }

    public static class ContactsBean {
        /**
         * contactName : zhangsan
         * createdTime : 2017-12-26 17:42:30
         * id : 301
         * remark : WTF
         * updatedTime : 2017-12-26 17:42:30
         * walletAddress : a
         * walletContact : ffjjfjfff
         * walletKey : ert
         * walletKeyHash : 100711
         */

        private String contactName;
        private String createdTime;
        private int id;
        private String remark;
        private String updatedTime;
        private String walletAddress;
        private String walletContact;
        private String walletKey;
        private int walletKeyHash;

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public String getWalletAddress() {
            return walletAddress;
        }

        public void setWalletAddress(String walletAddress) {
            this.walletAddress = walletAddress;
        }

        public String getWalletContact() {
            return walletContact;
        }

        public void setWalletContact(String walletContact) {
            this.walletContact = walletContact;
        }

        public String getWalletKey() {
            return walletKey;
        }

        public void setWalletKey(String walletKey) {
            this.walletKey = walletKey;
        }

        public int getWalletKeyHash() {
            return walletKeyHash;
        }

        public void setWalletKeyHash(int walletKeyHash) {
            this.walletKeyHash = walletKeyHash;
        }
    }
}
