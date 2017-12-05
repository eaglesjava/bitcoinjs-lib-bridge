package com.bitbill.www.model.wallet.network.entity;

import com.bitbill.www.common.base.model.entity.Entity;

/**
 * Created by isanwenyu@163.com on 2017/12/5.
 */
public class TransactionRecord extends Entity {

    private int status;
    private String address;
    private String date;
    private int confirmCount;
    private long amount;

    public TransactionRecord(int status, String address, String date, int confirmCount, long amount) {
        this.status = status;
        this.address = address;
        this.date = date;
        this.confirmCount = confirmCount;
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public TransactionRecord setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public TransactionRecord setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDate() {
        return date;
    }

    public TransactionRecord setDate(String date) {
        this.date = date;
        return this;
    }

    public int getConfirmCount() {
        return confirmCount;
    }

    public TransactionRecord setConfirmCount(int confirmCount) {
        this.confirmCount = confirmCount;
        return this;
    }

    public long getAmount() {
        return amount;
    }

    public TransactionRecord setAmount(long amount) {
        this.amount = amount;
        return this;
    }
}
