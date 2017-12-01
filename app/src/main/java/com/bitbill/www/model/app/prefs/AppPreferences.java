/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.prefs;


import com.bitbill.www.common.base.model.prefs.Prefs;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public interface AppPreferences extends Prefs {

    boolean isGuideBrowsed();

    void setGuideBrowsed();
}
