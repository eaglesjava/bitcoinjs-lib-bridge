/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.network;

import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.model.wallet.network.entity.GetExchangeRateResponse;
import com.google.gson.reflect.TypeToken;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/7/28.
 */

@Singleton
public class AppApiHelper extends ApiHelper implements AppApi {
    @Inject
    public AppApiHelper(ApiHeader apiHeader, @BaseUrlInfo String baseurl) {
        super(apiHeader, baseurl);
    }

    @Override
    public Observable<ApiResponse<GetExchangeRateResponse>> getExchangeRate() {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_EXCHANGE_RATE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .build()
                .getParseObservable(new TypeToken<ApiResponse<GetExchangeRateResponse>>() {
                });
    }
}
