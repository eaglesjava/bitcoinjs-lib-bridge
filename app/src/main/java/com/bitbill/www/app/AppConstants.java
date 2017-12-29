/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.app;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public final class AppConstants {


    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;
    public static final int API_STATUS_CODE_SERVER_ERROR = 500;
    public static final String DB_NAME = "bitbill_database.db";
    public static final String PREF_APP_NAME = "bitbill_app_pref";
    public static final String PREF_WALLET_NAME = "bitbill_wallet_pref";
    public static final long NULL_INDEX = -1L;
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String EXTRA_WALLET = "extra_wallet";
    public static final String EXTRA_MNEMONIC = "etra_mnemonic";
    public static final String EXTRA_IS_CREATE_WALLET = "is_create_wallet";
    public static final String EXTRA_IS_FROM_ASSET = "is_from_asset";
    public static final String EXTRA_IS_RESET_PWD = "is_reset_pwd";
    public static final String EXTRA_SEND_ADDRESS = "send_address";
    public static final String EXTRA_SEND_AMOUNT = "send_amount";
    public static final String EXTRA_IS_SEND_ALL = "is_send_all";
    public static final String EXTRA_CONTACT = "extra_contact";
    public static final String EXTRA_RECEIVE_ADDRESS = "receive_address";
    public static final String EXTRA_RECEIVE_AMOUNT = "receive_amount";
    public static final String EXTRA_CONTACT_ADDRESS = "contact_address";
    public static final String EXTRA_WALLET_ID = "wallet_id";
    public static final String EXTRA_IS_FROM_SETTING = "is_from_setting";


    public static final String ARG_DATAS = "args_datas";
    public static final String ARG_WALLET = "arg_wallet";
    public static final String ARG_IS_SELECT = "is_select";
    /**
     * btc about
     */
    public static final String SCHEME_BITCOIN = "bitcoin";
    public static final long SATOSHI = 100000000;
    /**
     * socket event
     */
    public static final String EVENT_REGISTER = "register";
    public static final String EVENT_CONFIRM = "confirm";
    public static final String EVENT_UNCONFIRM = "unconfirm";
    public static final String PLATFORM = "Android";

    /**
     * cointype
     */
    public static final String BTC_COIN_TYPE = "BTC";

    /**
     * bitbill h5
     */
    public static final String SCHEME_BITBILL = "bitbill";
    public static final String HOST_BITBILL = "www.bitbill.com";
    public static final String PATH_CONTACT = "contact";
    public static final String QUERY_ID = "id";
    public static final String QUERY_AMOUNT = "amount";
    public static final String QUERY_ADDRESS = "address";

    private AppConstants() {
        // This utility class is not publicly instantiable
    }

}
