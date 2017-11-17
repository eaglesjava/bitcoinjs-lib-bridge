/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.network.api;


import com.bitbill.www.di.qualifier.BaseUrlInfo;

import javax.inject.Inject;

/**
 * Created by isanwenyu@163.com on 2017/7/24.
 */
public class ApiHelper implements Api {
    protected final ApiHeader mApiHeader;
    protected final String mBaseUrl;

    @Inject
    public ApiHelper(ApiHeader apiHeader, @BaseUrlInfo String baseUrl) {
        mApiHeader = apiHeader;
        mBaseUrl = baseUrl;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }
}
