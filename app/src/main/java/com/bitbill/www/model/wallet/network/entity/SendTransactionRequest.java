package com.bitbill.www.model.wallet.network.entity;

/**
 * extendedKeysHash	true	String	扩展公钥MD5
 * outAddress	true	String	收币地址
 * outAmount	true	double	转币金额
 * txHash	true	String	交易hash
 * hexTx	true	String	交易数据
 * Created by isanwenyu on 2017/12/18.
 */

public class SendTransactionRequest {

    private String extendedKeysHash;
    private String outAddress;
    private long outAmount;
    private String txHash;
    private String hexTx;

    public SendTransactionRequest(String extendedKeysHash, String outAddress, long outAmount, String txHash, String hexTx) {
        this.extendedKeysHash = extendedKeysHash;
        this.outAddress = outAddress;
        this.outAmount = outAmount;
        this.txHash = txHash;
        this.hexTx = hexTx;
    }

    public String getExtendedKeysHash() {
        return extendedKeysHash;
    }

    public void setExtendedKeysHash(String extendedKeysHash) {
        this.extendedKeysHash = extendedKeysHash;
    }

    public String getOutAddress() {
        return outAddress;
    }

    public void setOutAddress(String outAddress) {
        this.outAddress = outAddress;
    }

    public long getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(long outAmount) {
        this.outAmount = outAmount;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getHexTx() {
        return hexTx;
    }

    public void setHexTx(String hexTx) {
        this.hexTx = hexTx;
    }
}
