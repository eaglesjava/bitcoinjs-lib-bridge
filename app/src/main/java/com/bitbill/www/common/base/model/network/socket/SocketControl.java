/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.network.socket;

import io.socket.client.Socket;

/**
 * Created by isanwenyu@163.com on 2017/8/1.
 */
public interface SocketControl {
    /**
     * get socket instance
     *
     * @return
     */
    Socket getSocket();
}
