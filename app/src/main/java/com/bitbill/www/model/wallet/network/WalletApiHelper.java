package com.bitbill.www.model.wallet.network;

import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.google.gson.reflect.TypeToken;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
@Singleton
public class WalletApiHelper extends ApiHelper implements WalletApi {

    @Inject
    public WalletApiHelper(ApiHeader apiHeader, @BaseUrlInfo String baseUrl) {
        super(apiHeader, baseUrl);
    }

    @Override
    public Observable<ApiResponse<String>> createWallet(String walletId, String extendedKeys, String clientId, String deviceToken, long indexNo) {
        return Rx2AndroidNetworking.post(mBaseUrl)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter("walletId", walletId)
                .addBodyParameter("extendedKeys", extendedKeys)
                .addBodyParameter("clientId", clientId)
                .addBodyParameter("deviceToken", deviceToken)
                .addBodyParameter("indexNo", String.valueOf(indexNo))
                .build()
                .getParseObservable(new TypeToken<ApiResponse<String>>() {
                });
    }
}
