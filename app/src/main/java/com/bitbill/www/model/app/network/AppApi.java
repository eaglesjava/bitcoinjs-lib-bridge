/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.network;

import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.app.network.entity.FeeBackRequest;
import com.bitbill.www.model.app.network.entity.GetConfigResponse;
import com.bitbill.www.model.app.network.entity.GetExchangeRateResponse;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/7/28.
 */
public interface AppApi extends Api {

    /**
     * 获取btc兑换比例
     *
     * @return
     */
    Observable<ApiResponse<GetExchangeRateResponse>> getExchangeRate();


    /**
     * 获取配置信息
     *
     * @return
     */
    Observable<ApiResponse<GetConfigResponse>> getConfig();

    /**
     * 意见反馈
     *
     * @return
     */
    Observable<ApiResponse> feeBack(FeeBackRequest feeBackRequest);

    /**
     * 下载文件
     *
     * @param url
     * @param dirPath
     * @param fileName
     * @param downloadProgressListener
     * @param downloadListener
     */
    void downloadFile(String url, String dirPath, String fileName, DownloadProgressListener downloadProgressListener, DownloadListener downloadListener);

}
