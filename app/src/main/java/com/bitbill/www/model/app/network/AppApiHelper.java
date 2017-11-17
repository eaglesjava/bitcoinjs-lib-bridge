/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.network;

import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.di.qualifier.BaseUrlInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by isanwenyu@163.com on 2017/7/28.
 */

@Singleton
public class AppApiHelper extends ApiHelper implements AppApi {
    @Inject
    public AppApiHelper(ApiHeader apiHeader, @BaseUrlInfo String baseurl) {
        super(apiHeader, baseurl);
    }

}
