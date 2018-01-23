package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu@163.com on 2017/12/4.
 */
public class CreateWalletRequest {

    /**
     * walletId : sfddsaf
     * extendedKeys : xpub6ENbEGckwnay85QqMu2Lx5qvSUWEEyNvZgYzdUDSFcmGXebX4DrtdGYtXCVzrNhj3w8soVVA2YsqSuanf83WbbBMhpSvaMNBKdg8fqGsGe3
     * clientId : 816135258c55731c
     * deviceToken :
     */

    private String walletId;
    private String extendedKeys;//扩展公钥
    private String extendedChangeKeys;//内部公钥
    private String clientId;
    private String deviceToken;

    public CreateWalletRequest(String walletId, String extendedKeys, String extendedChangeKeys, String clientId, String deviceToken) {
        this.walletId = walletId;
        this.extendedKeys = extendedKeys;
        this.clientId = clientId;
        this.deviceToken = deviceToken;
        this.extendedChangeKeys = extendedChangeKeys;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getExtendedKeys() {
        return extendedKeys;
    }

    public void setExtendedKeys(String extendedKeys) {
        this.extendedKeys = extendedKeys;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
