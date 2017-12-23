package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class TxHistory {


    /**
     * amount : 0
     * createdTime : 2017-12-06 11:38:34
     * gatherAddressIn : 1Je57JoZNWeBJ1L4hZoGbB4y5dkn5BvCh
     * gatherAddressOut : 1HqkEpMYETMbkGxF7m9eJTRvhL7rF31yJC
     * inOut : 1
     * isUnConfirm : 1
     * txHash : 882a8ee48625399e90f039fce1189c805d3fd7d02416063e5b119f0eefac9205
     */

    private int amount;
    private String createdTime;
    private String gatherAddressIn;
    private String gatherAddressOut;
    private int inOut;
    private int isUnConfirm;
    private String txHash;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getGatherAddressIn() {
        return gatherAddressIn;
    }

    public void setGatherAddressIn(String gatherAddressIn) {
        this.gatherAddressIn = gatherAddressIn;
    }

    public String getGatherAddressOut() {
        return gatherAddressOut;
    }

    public void setGatherAddressOut(String gatherAddressOut) {
        this.gatherAddressOut = gatherAddressOut;
    }

    public int getInOut() {
        return inOut;
    }

    public void setInOut(int inOut) {
        this.inOut = inOut;
    }

    public int getIsUnConfirm() {
        return isUnConfirm;
    }

    public void setIsUnConfirm(int isUnConfirm) {
        this.isUnConfirm = isUnConfirm;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
