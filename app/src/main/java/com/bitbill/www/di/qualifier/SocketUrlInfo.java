/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketUrlInfo {
}
