/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.component;

import com.bitbill.www.di.module.ServiceModule;
import com.bitbill.www.di.scope.PerService;
import com.bitbill.www.service.SocketServiceProvider;
import com.bitbill.www.service.SyncService;

import dagger.Component;
import io.socket.client.Socket;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    Socket getSocket();

    void inject(SocketServiceProvider socketServiceProvider);

    void inject(SyncService syncService);
}
