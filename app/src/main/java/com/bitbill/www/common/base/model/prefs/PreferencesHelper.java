/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public class PreferencesHelper implements Prefs {
    protected final SharedPreferences mPrefs;

    public PreferencesHelper(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public void clear() {
        mPrefs.edit().clear().commit();
    }
}
