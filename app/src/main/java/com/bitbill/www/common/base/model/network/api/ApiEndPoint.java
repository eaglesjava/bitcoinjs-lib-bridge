/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.network.api;

import com.bitbill.www.BuildConfig;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public final class ApiEndPoint {
    private static final String BITBILL_BITCOIN = "/bitbill/bitcoin";
    public static final String WALLET_CREATE = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/create";
    public static final String WALLET_IMPORT = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/import";
    public static final String WALLET_DELETE = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/deleteWallet";
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
    public static final String GET_TX_LIST = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getTxList";
    public static final String LIST_UNCONFIRM_TX = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/listUnconfirmTx";
    public static final String GET_CONFIG = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getConfig";
    public static final String SEARCH_WALLETID = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/searchWalletId";
    public static final String ADD_CONTACTS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/addContacts";
    public static final String UPDATE_CONTACTS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/updateContacts";
    public static final String RECOVER_CONTACTS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/recoverContacts";
    public static final String DELETE_CONTACTS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/deleteContacts";
    public static final String GET_LAST_ADDRESS = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getLastAddress";
    public static final String GET_TXINFO = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getTxInfo";
    public static final String GET_EXCHANGE_RATE = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/get_exchange_rate";
    public static final String GET_CACHE_VERSION = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/wallet/getCacheVersion";
    public static final String FEED_BACK = BuildConfig.BASE_URL + BITBILL_BITCOIN
            + "/feed_back";
    private ApiEndPoint() {
        // This class is not publicly instantiable
    }


}
