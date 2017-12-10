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
    public static final String PREF_WALLET_NAME = "bitbill_wallet_pref";
    public static final long NULL_INDEX = -1L;
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String EXTRA_WALLET = "extra_wallet";
    public static final String EXTRA_MNEMONIC = "etra_mnemonic";
    public static final String EXTRA_IS_CREATE_WALLET = "is_create_wallet";
    public static final String IS_BTC_RECOD = "is_btc_record";
    public static final String ARG_WALLET = "arg_wallet";
    public static final String EXTRA_IS_FROM_GUIDE = "extra_is_from_guide";

    private AppConstants() {
        // This utility class is not publicly instantiable
    }

}
