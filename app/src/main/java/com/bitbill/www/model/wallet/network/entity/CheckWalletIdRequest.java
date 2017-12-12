package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public class CheckWalletIdRequest {

    private String walletId;//钱包名称

    public CheckWalletIdRequest(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletId() {
        return walletId;
    }

    public CheckWalletIdRequest setWalletId(String walletId) {
        this.walletId = walletId;
        return this;
    }
}
