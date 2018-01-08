package com.bitbill.www.model.address.network;

import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.model.address.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.address.network.entity.RefreshAddressResponse;
import com.google.gson.reflect.TypeToken;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/8.
 */

@Singleton
public class AddressApiHelper extends ApiHelper implements AddressApi {

    @Inject
    public AddressApiHelper(ApiHeader apiHeader, @BaseUrlInfo String baseUrl) {
        super(apiHeader, baseUrl);
    }

    /**
     * 扫描地址
     *
     * @param refreshAddressRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<RefreshAddressResponse>> refreshAddress(RefreshAddressRequest refreshAddressRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.REFRESH_ADDRESS)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(refreshAddressRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<RefreshAddressResponse>>() {
                });
    }
}
