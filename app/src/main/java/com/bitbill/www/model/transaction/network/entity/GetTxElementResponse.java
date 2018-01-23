package com.bitbill.www.model.transaction.network.entity;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public class GetTxElementResponse {
    private List<UtxoBean> utxo;
    private List<FeesBean> fees;

    public List<UtxoBean> getUtxo() {
        return utxo;
    }

    public void setUtxo(List<UtxoBean> utxo) {
        this.utxo = utxo;
    }

    public List<FeesBean> getFees() {
        return fees;
    }

    public void setFees(List<FeesBean> fees) {
        this.fees = fees;
    }

    public static class UtxoBean {
        /**
         * addressIndex : 4
         * addressTxt : 17fLtpDmu7GhMgFyVBCrNySSanodr3toXP
         * availableforspending : true
         * createdTime : 2017-12-16 14:14:23
         * hashAddress : 1126050813
         * id : 30946
         * indexNo : 49
         * isDeleted : false
         * outTransactionHashcode : 953885679
         * outValue : 10000
         * reqSings : 1
         * sumOutAmount : 10000
         * txHash : d2f3501936f9a57ec2a3aac14b01bb693c376bf4978f13eba61dba3f5bb9d54d
         * txid : 12102
         * updatedTime : 2017-12-16 14:14:23
         * vIndex : 0
         * vType : pubkeyhash
         * walletId : 105
         */

        private int addressIndex;
        private String addressTxt;
        private boolean availableforspending;
        private String createdTime;
        private int hashAddress;
        private int id;
        private int indexNo;
        private boolean isDeleted;
        private int outTransactionHashcode;
        private String outValue;
        private int reqSings;
        private int sumOutAmount;
        private String txHash;
        private String txid;
        private String updatedTime;
        private int vIndex;
        private String vType;
        private int walletId;
        private boolean isChange;
        private int addressType;//地址类型0:接收地址，1：找零地址

        public boolean isChange() {
            return isChange;
        }

        public void setChange(boolean change) {
            isChange = change;
        }

        public int getAddressIndex() {
            return addressIndex;
        }

        public void setAddressIndex(int addressIndex) {
            this.addressIndex = addressIndex;
        }

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

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
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

        public int getIndexNo() {
            return indexNo;
        }

        public void setIndexNo(int indexNo) {
            this.indexNo = indexNo;
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

        public int getSumOutAmount() {
            return sumOutAmount;
        }

        public void setSumOutAmount(int sumOutAmount) {
            this.sumOutAmount = sumOutAmount;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
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

        public int getWalletId() {
            return walletId;
        }

        public void setWalletId(int walletId) {
            this.walletId = walletId;
        }

        public int getAddressType() {
            return addressType;
        }

        public void setAddressType(int addressType) {
            this.addressType = addressType;
        }
    }

    public static class FeesBean {
        /**
         * best : false
         * fee : 420
         * time : 10
         */

        private boolean best;
        private int fee;
        private int time;

        public boolean isBest() {
            return best;
        }

        public void setBest(boolean best) {
            this.best = best;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
