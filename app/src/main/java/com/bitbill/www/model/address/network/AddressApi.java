package com.bitbill.www.model.address.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.address.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.address.network.entity.RefreshAddressResponse;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/8.
 */

public interface AddressApi extends Api {

    /**
     * 扫描地址
     *
     * @param refreshAddressRequest
     * @return
     */
    Observable<ApiResponse<RefreshAddressResponse>> refreshAddress(RefreshAddressRequest refreshAddressRequest);
}
