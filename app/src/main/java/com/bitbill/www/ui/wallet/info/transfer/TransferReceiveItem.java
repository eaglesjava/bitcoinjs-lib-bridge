package com.bitbill.www.ui.wallet.info.transfer;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferReceiveItem extends TransferItem {
    private String address;
    private long amount;

    public String getAddress() {
        return address;
    }

    public TransferReceiveItem setAddress(String address) {
        this.address = address;
        return this;
    }

    public long getAmount() {
        return amount;
    }

    public TransferReceiveItem setAmount(long amount) {
        this.amount = amount;
        return this;
    }
}
