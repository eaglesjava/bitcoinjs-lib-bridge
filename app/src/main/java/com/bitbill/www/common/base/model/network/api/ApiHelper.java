/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.network.api;


/**
 * Created by isanwenyu@163.com on 2017/7/24.
 */
public class ApiHelper implements Api {
    protected final ApiHeader mApiHeader;
    protected final String mBaseUrl;

    public ApiHelper(ApiHeader apiHeader, String baseUrl) {
        mApiHeader = apiHeader;
        mBaseUrl = baseUrl;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }
}
