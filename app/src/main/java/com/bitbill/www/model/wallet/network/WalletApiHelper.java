package com.bitbill.www.model.wallet.network;

import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.DeleteWalletRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetCacheVersionRequest;
import com.bitbill.www.model.wallet.network.entity.GetConfigResponse;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletResponse;
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
    public WalletApiHelper(ApiHeader apiHeader, @BaseUrlInfo java.lang.String baseUrl) {
        super(apiHeader, baseUrl);
    }

    @Override
    public Observable<ApiResponse> createWallet(CreateWalletRequest createWalletRequest) {

        return Rx2AndroidNetworking.post(ApiEndPoint.WALLET_CREATE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(createWalletRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse>() {
                });
    }

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<ImportWalletResponse>> importWallet(ImportWalletRequest importWalletRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.WALLET_IMPORT)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(importWalletRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<ImportWalletResponse>>() {
                });
    }

    @Override
    public Observable<ApiResponse> deleteWallet(DeleteWalletRequest deleteWalletRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.WALLET_DELETE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(deleteWalletRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse>() {
                });
    }

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse> checkWalletId(CheckWalletIdRequest checkWalletIdRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CHECK_WALLETID)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(checkWalletIdRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse>() {
                });
    }

    /**
     * 获取walletId
     *
     * @param getWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<GetWalletIdResponse>> getWalletId(GetWalletIdRequest getWalletIdRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_WALLETID)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getWalletIdRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<GetWalletIdResponse>>() {
                });
    }

    /**
     * 查询余额
     *
     * @param getBalanceRequest
     * @return
     */
    @Override
    public Observable<ApiResponse> getBalance(GetBalanceRequest getBalanceRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_BALANCE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getBalanceRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse>() {
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
    public Observable<ApiResponse> getCacheVersion(GetCacheVersionRequest getCacheVersionRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_CACHE_VERSION)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getCacheVersionRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse>() {
                });
    }

}
