package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class GetExchangeRateResponse {
    /**
     * cnyrate : 92682.67
     * usdrate : 13905.2
     */

    private double cnyrate;
    private double usdrate;

    public double getCnyrate() {
        return cnyrate;
    }

    public void setCnyrate(double cnyrate) {
        this.cnyrate = cnyrate;
    }

    public double getUsdrate() {
        return usdrate;
    }

    public void setUsdrate(double usdrate) {
        this.usdrate = usdrate;
    }
}
