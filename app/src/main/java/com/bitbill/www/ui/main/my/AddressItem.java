package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.model.entity.TitleItem;

public class AddressItem extends TitleItem {
    private String address;
    private long balance;

    public AddressItem(String address, long balance) {
        this.address = address;
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public static class ExtendedAddressItem extends AddressItem {


        public ExtendedAddressItem(String address, long balance) {
            super(address, balance);
        }
    }

    public static class InternalAddressItem extends AddressItem {

        public InternalAddressItem(String address, long balance) {
            super(address, balance);
        }
    }
}