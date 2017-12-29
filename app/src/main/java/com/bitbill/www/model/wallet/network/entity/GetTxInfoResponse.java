package com.bitbill.www.model.wallet.network.entity;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class GetTxInfoResponse {

    /**
     * createdTime : 2017-12-26 20:23:52
     * height : 500000
     * inputs : [{"address":"1LHA1sF7BD1ioCRCJSBqfYbngRe9zqNB2B"},{"address":"1BwW7cusBZhrLVSj9j3yZqBHrR64SPAqmb"}]
     * outputs : [{"address":"1NzN4y4eZZjtKVZjCVWkCpeqb9mTmAvCWt","value":69302},{"address":"1cmY5nUiSEDuzWPL9tYjAsVa5PcGfJwwM","value":12900349}]
     * txHash : 83eeaecaf531e5239ffc3ba7ff583c696f7dbe3610f0d672d41e0b9443632c82
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
         * address : 1LHA1sF7BD1ioCRCJSBqfYbngRe9zqNB2B
         */

        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class OutputsBean {
        /**
         * address : 1NzN4y4eZZjtKVZjCVWkCpeqb9mTmAvCWt
         * value : 69302
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
}
