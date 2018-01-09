package com.bitbill.www.ui.wallet.info.transfer;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferSendItem extends TransferItem {

    String address;
    long amount;

    public String getAddress() {
        return address;
    }

    public TransferSendItem setAddress(String address) {
        this.address = address;
        return this;
    }

    public long getAmount() {
        return amount;
    }

    public TransferSendItem setAmount(long amount) {
        this.amount = amount;
        return this;
    }
}
