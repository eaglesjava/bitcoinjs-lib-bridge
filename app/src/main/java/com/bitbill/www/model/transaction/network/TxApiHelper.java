package com.bitbill.www.model.transaction.network;

import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
import com.bitbill.www.model.transaction.network.entity.GetTxElement;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.transaction.network.entity.GetTxListRequest;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionResponse;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.google.gson.reflect.TypeToken;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/10.
 */

@Singleton
public class TxApiHelper extends ApiHelper implements TxApi {
    @Inject
    public TxApiHelper(ApiHeader apiHeader, @BaseUrlInfo String baseUrl) {
        super(apiHeader, baseUrl);
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
    public Observable<ApiResponse<ListTxElementResponse>> getTxList(GetTxListRequest getTxListRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_TX_LIST)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getTxListRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<ListTxElementResponse>>() {
                });
    }

    /**
     * 未确认交易列表
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.LIST_UNCONFIRM_TX)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(listUnconfirmRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<ListTxElementResponse>>() {
                });
    }

    /**
     * @return
     */
    @Override
    public Observable<ApiResponse<TxElement>> getTxInfo(GetTxInfoRequest getTxInfoRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_TXINFO)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getTxInfoRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<TxElement>>() {
                });
    }
}
