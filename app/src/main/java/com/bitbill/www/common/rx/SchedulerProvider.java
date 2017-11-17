/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.rx;

import io.reactivex.Scheduler;

/**
 * Created by isanwenyu@163.com on 17/07/2017.
 */

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();

}
