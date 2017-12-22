package com.bitbill.www.crypto.entity;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public class Transaction {
    private List<Input> inputs;
    private List<Output> outputs;

    public Transaction(List<Input> inputs, List<Output> ouputs) {
        this.inputs = inputs;
        this.outputs = ouputs;
    }

    public static class Input {
        private String txHash;
        private int index;
        private int bip39Index;

        public Input(String txHash, int index, int bip39Index) {
            this.txHash = txHash;
            this.index = index;
            this.bip39Index = bip39Index;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getBip39Index() {
            return bip39Index;
        }

        public void setBip39Index(int bip39Index) {
            this.bip39Index = bip39Index;
        }
    }

    public static class Output {
        private String address;
        private long amount;//unit satoshi

        public Output(String address, long amount) {
            this.address = address;
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

    }
}
