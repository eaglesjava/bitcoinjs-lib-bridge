package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;

/**
 * Created by isanwenyu on 2018/1/25.
 */

public class SocketServerStateEvent extends MessageEvent {
    private ServerState state;

    public SocketServerStateEvent(ServerState state) {
        this.state = state;
    }

    public ServerState getState() {
        return state;
    }

    public enum ServerState {
        connected,
        disConnect,
        connectTimeout,
        connectError
    }
}
