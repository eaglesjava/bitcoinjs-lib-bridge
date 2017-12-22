package com.bitbill.www.model.wallet.network.socket;

import com.bitbill.www.common.base.model.entity.Entity;

/**
 * Created by isanwenyu on 2017/12/21.
 */

public class Register extends Entity {
    private String walletId;
    private String deviceToken;
    private String clientId;
    private String platform;

    public Register(String walletId, String deviceToken, String clientId, String platform) {
        this.walletId = walletId;
        this.deviceToken = deviceToken;
        this.clientId = clientId;
        this.platform = platform;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}
