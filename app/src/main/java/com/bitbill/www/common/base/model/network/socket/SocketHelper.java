/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.network.socket;


import com.bitbill.www.di.qualifier.SocketUrlInfo;

import java.net.URISyntaxException;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

/**
 * Created by isanwenyu@163.com on 2017/8/1.
 */
public class SocketHelper implements SocketControl {
    public static final int SOCKET_TIME_OUT = 20000;
    private static final String TAG = "SocketHelper";
    private Socket mSocket;

    @Inject
    public SocketHelper(@SocketUrlInfo String socketUrl) {
        try {
            //support for ws:// protocol
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            opts.timeout = SOCKET_TIME_OUT;
            mSocket = IO.socket(socketUrl, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * get socket instance
     *
     * @return
     */
    @Override
    public Socket getSocket() {
        return mSocket;
    }
}
