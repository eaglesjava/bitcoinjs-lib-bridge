package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;

/**
 * Created by isanwenyu on 2018/2/5.
 */

public class PwdEditEvent extends MessageEvent {

    private String mS;

    public PwdEditEvent(String s) {
        mS = s;
    }

    public String getS() {
        return mS;
    }

    public void setS(String s) {
        mS = s;
    }
}
