package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class Unconfirm {

    /**
     * addressContext : 1Je57JoZNWeBJ1L4hZoGbB4y5dkn5BvChA
     * addressHash : -99766520
     * createdTime : 2017-12-05 14:43:29
     * gatherAddressIn : 1Je57JoZNWeBJ1L4hZoGbB4y5dkn5BvCh
     * gatherAddressOut : 1HqkEpMYETMbkGxF7m9eJTRvhL7rF31yJC
     * id : 8
     * inOut : 1
     * isDeleted : false
     * memoryPoolTxHash : -1861153561
     * memoryTx : 882a8ee48625399e90f039fce1189c805d3fd7d02416063e5b119f0eefac9205
     * sumAmount : 4.67E-5
     * txStatus : 1
     * updatedTime : 2017-12-05 15:43:29
     * vanishTime : 0
     */

    private String addressContext;
    private int addressHash;
    private String createdTime;
    private String gatherAddressIn;
    private String gatherAddressOut;
    private int id;
    private int inOut;
    private boolean isDeleted;
    private int memoryPoolTxHash;
    private String memoryTx;
    private double sumAmount;
    private int txStatus;
    private String updatedTime;
    private int vanishTime;

    public String getAddressContext() {
        return addressContext;
    }

    public void setAddressContext(String addressContext) {
        this.addressContext = addressContext;
    }

    public int getAddressHash() {
        return addressHash;
    }

    public void setAddressHash(int addressHash) {
        this.addressHash = addressHash;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInOut() {
        return inOut;
    }

    public void setInOut(int inOut) {
        this.inOut = inOut;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getMemoryPoolTxHash() {
        return memoryPoolTxHash;
    }

    public void setMemoryPoolTxHash(int memoryPoolTxHash) {
        this.memoryPoolTxHash = memoryPoolTxHash;
    }

    public String getMemoryTx() {
        return memoryTx;
    }

    public void setMemoryTx(String memoryTx) {
        this.memoryTx = memoryTx;
    }

    public double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(double sumAmount) {
        this.sumAmount = sumAmount;
    }

    public int getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(int txStatus) {
        this.txStatus = txStatus;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getVanishTime() {
        return vanishTime;
    }

    public void setVanishTime(int vanishTime) {
        this.vanishTime = vanishTime;
    }
}
