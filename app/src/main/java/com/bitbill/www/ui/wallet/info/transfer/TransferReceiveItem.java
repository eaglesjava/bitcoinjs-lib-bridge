package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.model.entity.TitleItem;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferReceiveItem extends TitleItem {
    private String address;
    private long amount;
    private boolean isMine;
    private boolean isInternal;

    public TransferReceiveItem(String address, long amount, boolean isMine, boolean isInternal) {
        this.address = address;
        this.amount = amount;
        this.isMine = isMine;
        this.isInternal = isInternal;
    }
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


    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }
}
