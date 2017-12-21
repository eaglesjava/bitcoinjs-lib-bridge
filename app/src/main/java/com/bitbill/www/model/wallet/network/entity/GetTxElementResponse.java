package com.bitbill.www.model.wallet.network.entity;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public class GetTxElementResponse {

    private List<FeesBean> fees;
    private List<UtxoBean> utxo;

    public List<FeesBean> getFees() {
        return fees;
    }

    public void setFees(List<FeesBean> fees) {
        this.fees = fees;
    }

    public List<UtxoBean> getUtxo() {
        return utxo;
    }

    public void setUtxo(List<UtxoBean> utxo) {
        this.utxo = utxo;
    }

    public static class FeesBean {
        /**
         * best : false
         * fee : 59999.99999999999
         * time : 10
         */

        private boolean best;
        private long fee;
        private int time;

        public boolean isBest() {
            return best;
        }

        public void setBest(boolean best) {
            this.best = best;
        }

        public long getFee() {
            return fee;
        }

        public void setFee(long fee) {
            this.fee = fee;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

    public static class UtxoBean {
        /**
         * addressTxt : 1DJkjSqW9cX9XWdU71WX3Aw6s6Mk4C3TtN
         * availableforspending : true
         * createdTime : 1512028522000
         * hashAddress : 1549104517
         * id : 1079
         * isDeleted : false
         * outTransactionHashcode : 1715890813
         * outValue : 50
         * reqSings : 1
         * spentby : null
         * sumOutAmount : 50.2
         * txId : 1075
         * updatedTime : 1512028522000
         * vIndex : 0
         * vType : pubkey
         * walletId : 12
         */

        private String addressTxt;
        private boolean availableforspending;
        private long createdTime;
        private int hashAddress;
        private int id;
        private boolean isDeleted;
        private int outTransactionHashcode;
        private String outValue;
        private int reqSings;
        private Object spentby;
        private double sumOutAmount;
        private int txId;
        private long updatedTime;
        private int vIndex;
        private String vType;
        private String walletId;
        private String txHash;
        private int addressIndex;

        public String getAddressTxt() {
            return addressTxt;
        }

        public void setAddressTxt(String addressTxt) {
            this.addressTxt = addressTxt;
        }

        public boolean isAvailableforspending() {
            return availableforspending;
        }

        public void setAvailableforspending(boolean availableforspending) {
            this.availableforspending = availableforspending;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        public int getHashAddress() {
            return hashAddress;
        }

        public void setHashAddress(int hashAddress) {
            this.hashAddress = hashAddress;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public int getOutTransactionHashcode() {
            return outTransactionHashcode;
        }

        public void setOutTransactionHashcode(int outTransactionHashcode) {
            this.outTransactionHashcode = outTransactionHashcode;
        }

        public String getOutValue() {
            return outValue;
        }

        public void setOutValue(String outValue) {
            this.outValue = outValue;
        }

        public int getReqSings() {
            return reqSings;
        }

        public void setReqSings(int reqSings) {
            this.reqSings = reqSings;
        }

        public Object getSpentby() {
            return spentby;
        }

        public void setSpentby(Object spentby) {
            this.spentby = spentby;
        }

        public double getSumOutAmount() {
            return sumOutAmount;
        }

        public void setSumOutAmount(double sumOutAmount) {
            this.sumOutAmount = sumOutAmount;
        }

        public int getTxId() {
            return txId;
        }

        public void setTxId(int txId) {
            this.txId = txId;
        }

        public long getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(long updatedTime) {
            this.updatedTime = updatedTime;
        }

        public int getVIndex() {
            return vIndex;
        }

        public void setVIndex(int vIndex) {
            this.vIndex = vIndex;
        }

        public String getVType() {
            return vType;
        }

        public void setVType(String vType) {
            this.vType = vType;
        }

        public String getWalletId() {
            return walletId;
        }

        public void setWalletId(String walletId) {
            this.walletId = walletId;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public int getAddressIndex() {
            return addressIndex;
        }

        public void setAddressIndex(int addressIndex) {
            this.addressIndex = addressIndex;
        }
    }
}
