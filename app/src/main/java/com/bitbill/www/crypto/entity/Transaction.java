package com.bitbill.www.crypto.entity;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/18.
 */

public class Transaction {
    private List<Input> inputs;
    private List<Output> ouputs;

    public Transaction(List<Input> inputs, List<Output> ouputs) {
        this.inputs = inputs;
        this.ouputs = ouputs;
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
    }

    public static class Output {
        private String address;
        private long amount;//unit satoshi

        public Output(String address, long amount) {
            this.address = address;
            this.amount = amount;
        }
    }
}
