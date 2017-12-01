/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.prefs;

import android.content.Context;

import com.bitbill.www.common.base.model.prefs.PreferencesHelper;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.di.qualifier.PrefersAppInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */

@Singleton
public class AppPreferencesHelper extends PreferencesHelper implements AppPreferences {

    public static final String IS_GUIDE_BROWSED = "is_guide_browsed";

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context, @PrefersAppInfo String prefFileName) {
        super(context, prefFileName);
    }

    @Override
    public boolean isGuideBrowsed() {
        return mPrefs.getBoolean(IS_GUIDE_BROWSED, false);
    }

    @Override
    public void setGuideBrowsed() {
        mPrefs.edit().putBoolean(IS_GUIDE_BROWSED, true).apply();

    }
}
