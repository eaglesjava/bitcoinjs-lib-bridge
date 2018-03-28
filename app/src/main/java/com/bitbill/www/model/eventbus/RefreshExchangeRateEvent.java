package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;

/**
 * Created by isanwenyu on 2018/3/28.
 */

public class RefreshExchangeRateEvent extends MessageEvent {

    private final double mCnyrate;
    private final double mUsdrate;

    public RefreshExchangeRateEvent(double cnyrate, double usdrate) {
        mCnyrate = cnyrate;
        mUsdrate = usdrate;
    }
}
