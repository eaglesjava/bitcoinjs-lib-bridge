/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.entity.eventbus;


import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;

/**
 * Created by isanwenyu@163.com on 2017/7/18.
 */
public class NetWorkChangedEvent extends MessageEvent {

    public static final int NETWORK_AVAILABLE = 1;

    public static final int NETWORK_NOT_AVAILABLE = 0;

    public NetWorkChangedEvent(int code, String msg) {
        super(code, msg);
    }
}
