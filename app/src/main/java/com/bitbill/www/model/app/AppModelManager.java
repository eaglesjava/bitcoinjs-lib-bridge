/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app;


import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.model.app.network.AppApi;
import com.bitbill.www.model.app.prefs.AppPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */

@Singleton
public class AppModelManager extends ModelManager implements AppModel {

    private final AppApi mAppApi;
    private final AppPreferences mAppPreferences;

    @Inject
    public AppModelManager(AppPreferences appPreferences, AppApi appApi) {
        mAppApi = appApi;
        mAppPreferences = appPreferences;
    }


    @Override
    public void clear() {
        mAppPreferences.clear();
    }

    @Override
    public ApiHeader getApiHeader() {
        return mAppApi.getApiHeader();
    }

    @Override
    public void setReceiveRemindDialogShown() {
        mAppPreferences.setReceiveRemindDialogShown();
    }

    @Override
    public boolean isReceiveRemindDialogShown() {
        return mAppPreferences.isReceiveRemindDialogShown();
    }

    @Override
    public boolean isShortcutShown() {
        return mAppPreferences.isShortcutShown();
    }

    @Override
    public void setShortcutShown(boolean shown) {
        mAppPreferences.setShortcutShown(shown);
    }
}
