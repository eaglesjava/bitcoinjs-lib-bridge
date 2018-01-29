/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.app.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.wallet.network.entity.GetExchangeRateResponse;

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
}
