package com.bitbill.www.model.wallet.network.entity;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class GetContactsResponse {


    /**
     * page : 1
     * records : 1
     * rows : [{"createdTime":"2017-12-20 14:52:43","id":39,"updatedTime":"2017-12-20 14:52:43","walletAddress":"12232132","walletKey":"abc","walletKeyHash":96354}]
     * total : 1
     */

    private String page;
    private String records;
    private String total;
    private List<RowsBean> rows;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * createdTime : 2017-12-20 14:52:43
         * id : 39
         * updatedTime : 2017-12-20 14:52:43
         * walletAddress : 12232132
         * walletKey : abc
         * walletKeyHash : 96354
         */

        private String createdTime;
        private int id;
        private String updatedTime;
        private String walletAddress;
        private String walletKey;
        private int walletKeyHash;

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
