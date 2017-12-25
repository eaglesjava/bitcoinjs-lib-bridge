/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.network.api;

import com.bitbill.www.BuildConfig;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public final class ApiEndPoint {
    public static final String A = "http://192.168.1.11:8086/a";
    private static final String BITBILL_BITCOIN = "/bitbill/bitcoin";
    public static final String WALLET_CREATE = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/create";
    public static final String WALLET_IMPORT = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/import";
    public static final String CHECK_WALLETID = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/checkWalletId";
    public static final String GET_WALLETID = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getWalletId";
    public static final String REFRESH_ADDRESS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/refreshAddress";
    public static final String GET_TX_ELEMENT = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getTxElement";

    public static final String SEND_TRANSACTION = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/sendTransaction";
    public static final String GET_BALANCE = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getBalance";
    public static final String GET_TX_HISTORY = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getTxHistory";
    public static final String LIST_UNCONFIRM = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/listUnconfirm";
    public static final String GET_CONFIG = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getConfig";
    public static final String SEARCH_WALLETID = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/searchWalletId";
    public static final String GET_LAST_ADDRESS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getLastAddress";

    private ApiEndPoint() {
        // This class is not publicly instantiable
    }


}
