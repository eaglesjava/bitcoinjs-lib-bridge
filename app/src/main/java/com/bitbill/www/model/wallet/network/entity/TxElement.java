package com.bitbill.www.model.wallet.network.entity;

import java.util.List;

public class TxElement {
    /**
     * createdTime : 2017-12-19 16:51:47
     * height : -1
     * inputs : [{"address":"17fLtpDmu7GhMgFyVBCrNySSanodr3toXP","value":0.001}]
     * outputs : [{"address":"17fLtpDmu7GhMgFyVBCrNySSanodr3toXP","value":0.001}]
     * txHash : 882a8ee48625399e90f039fce1189c805d3fd7d02416063e5b119f0eefac9205
     */

    private String createdTime;
    private int height;
    private String txHash;
    private List<InputsBean> inputs;
    private List<OutputsBean> outputs;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public List<InputsBean> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputsBean> inputs) {
        this.inputs = inputs;
    }

    public List<OutputsBean> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputsBean> outputs) {
        this.outputs = outputs;
    }

    public static class InputsBean {
        /**
         * address : 17fLtpDmu7GhMgFyVBCrNySSanodr3toXP
         * value : 0.001
         */

        private String address;
        private long value;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

    public static class OutputsBean {
        /**
         * address : 17fLtpDmu7GhMgFyVBCrNySSanodr3toXP
         * value : 0.001
         */

        private String address;
        private long value;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }
}