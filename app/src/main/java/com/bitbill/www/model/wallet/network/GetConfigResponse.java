package com.bitbill.www.model.wallet.network;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
public class GetConfigResponse {


    /**
     * aversion : 1.0
     * aforceVersion : 1.0
     * iforceVersion : 1.0
     * iversion : 1.0
     */

    private String aversion;
    private String aforceVersion;
    private String iforceVersion;
    private String iversion;

    public String getAversion() {
        return aversion;
    }

    public void setAversion(String aversion) {
        this.aversion = aversion;
    }

    public String getAforceVersion() {
        return aforceVersion;
    }

    public void setAforceVersion(String aforceVersion) {
        this.aforceVersion = aforceVersion;
    }

    public String getIforceVersion() {
        return iforceVersion;
    }

    public void setIforceVersion(String iforceVersion) {
        this.iforceVersion = iforceVersion;
    }

    public String getIversion() {
        return iversion;
    }

    public void setIversion(String iversion) {
        this.iversion = iversion;
    }
}
