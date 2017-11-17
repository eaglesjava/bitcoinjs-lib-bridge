/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}

