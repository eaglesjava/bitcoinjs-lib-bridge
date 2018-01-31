/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.network;

import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.model.app.network.entity.FeeBackRequest;
import com.bitbill.www.model.app.network.entity.GetConfigResponse;
import com.bitbill.www.model.app.network.entity.GetExchangeRateResponse;
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


    /**
     * 获取配置信息
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<GetConfigResponse>> getConfig() {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_CONFIG)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .build()
                .getParseObservable(new TypeToken<ApiResponse<GetConfigResponse>>() {
                });
    }

    @Override
    public Observable<ApiResponse> feeBack(FeeBackRequest feeBackRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.FEED_BACK)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(feeBackRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse>() {
                });
    }

    @Override
    public void downloadFile(String url, String dirPath, String fileName, DownloadProgressListener downloadProgressListener, DownloadListener downloadListener) {
        Rx2AndroidNetworking.download(url, dirPath, fileName)
                .setTag("download_file")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(downloadProgressListener)
                .startDownload(downloadListener);
    }
}
