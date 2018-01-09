package com.bitbill.www.model.wallet.network.entity;

import com.bitbill.www.common.base.model.entity.Entity;
import com.bitbill.www.common.utils.StringUtils;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class TxItem extends Entity {

    private String gatherAddressIn;
    private String gatherAddressOut;
    private InOut inOut = InOut.IN;//0: 转移,1：in（接收）,2：out(发送)
    private long sumAmount;
    private String txHash;
    private String inWalletId;
    private String outWalletId;
    private long height;
    private String createdTime;
    private List<TxElement.InputsBean> mInputs;
    private List<TxElement.OutputsBean> mOutputs;
    private String mRemark;

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

    public InOut getInOut() {
        return inOut;
    }

    public TxItem setInOut(InOut inOut) {
        this.inOut = inOut;
        return this;
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public TxItem setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
        return this;
    }

    public String getTxHash() {
        return txHash;
    }

    public TxItem setTxHash(String txHash) {
        this.txHash = txHash;
        return this;
    }

    public String getInWalletId() {
        return inWalletId;
    }

    public TxItem setInWalletId(String inWalletId) {
        this.inWalletId = inWalletId;
        return this;
    }

    public String getOutWalletId() {
        return outWalletId;
    }

    public TxItem setOutWalletId(String outWalletId) {
        this.outWalletId = outWalletId;
        return this;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public TxItem setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public List<TxElement.InputsBean> getInputs() {
        return mInputs;
    }

    public void setInputs(List<TxElement.InputsBean> inputs) {
        mInputs = inputs;
    }

    public List<TxElement.OutputsBean> getOutputs() {
        return mOutputs;
    }

    public void setOutputs(List<TxElement.OutputsBean> outputs) {
        mOutputs = outputs;
    }

    public String getRemark() {
        return StringUtils.isEmpty(mRemark) ? "无" : mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public enum InOut {
        TRANSFER,
        IN,
        OUT
    }
}

