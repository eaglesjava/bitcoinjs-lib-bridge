package com.bitbill.www.model.wallet.network;

import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetTxElement;
import com.bitbill.www.model.wallet.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxHistoryRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressResponse;
import com.bitbill.www.model.wallet.network.entity.SendTransactionRequest;
import com.bitbill.www.model.wallet.network.entity.SendTransactionResponse;
import com.bitbill.www.model.wallet.network.entity.TxHistory;
import com.bitbill.www.model.wallet.network.entity.Unconfirm;
import com.google.gson.reflect.TypeToken;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.List;

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
    public Observable<ApiResponse<java.lang.String>> createWallet(CreateWalletRequest createWalletRequest) {

        return Rx2AndroidNetworking.post(ApiEndPoint.WALLET_CREATE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(createWalletRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<java.lang.String>>() {
                });
    }

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<java.lang.String>> importWallet(ImportWalletRequest importWalletRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.WALLET_IMPORT)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(importWalletRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<java.lang.String>>() {
                });
    }

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<java.lang.String>> checkWalletId(CheckWalletIdRequest checkWalletIdRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CHECK_WALLETID)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(checkWalletIdRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<java.lang.String>>() {
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

    @Override
    public Observable<ApiResponse<GetTxElementResponse>> getTxElement(GetTxElement getTxElement) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_TX_ELEMENT)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getTxElement)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<GetTxElementResponse>>() {
                });
    }

    @Override
    public Observable<ApiResponse<SendTransactionResponse>> sendTransaction(SendTransactionRequest sendTransactionRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.SEND_TRANSACTION)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(sendTransactionRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<SendTransactionResponse>>() {
                });
    }

    /**
     * 交易记录
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<List<TxHistory>>> getTxHistory(GetTxHistoryRequest getTxHistoryRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_TX_HISTORY)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getTxHistoryRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<List<TxHistory>>>() {
                });
    }

    /**
     * 未确认交易列表
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<List<Unconfirm>>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.LIST_UNCONFIRM)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(listUnconfirmRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<List<Unconfirm>>>() {
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

}
