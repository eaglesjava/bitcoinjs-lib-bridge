package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;

/**
 * Created by isanwenyu on 2018/1/30.
 */

public class AppBackgroundEvent extends MessageEvent {
    private boolean isBackground;

    public AppBackgroundEvent(boolean isBackground) {
        this.isBackground = isBackground;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }
}
