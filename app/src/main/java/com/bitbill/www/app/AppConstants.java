/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.app;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public final class AppConstants {

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final String DB_NAME = "bitbill_database.db";
    public static final String PREF_APP_NAME = "bitbill_app_pref";
    public static final String PREF_USER_NAME = "bitbill_user_pref";

    public static final long NULL_INDEX = -1L;

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
