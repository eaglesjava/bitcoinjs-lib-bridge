package com.bitbill.www.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by isanwenyu on 2018/1/25.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
