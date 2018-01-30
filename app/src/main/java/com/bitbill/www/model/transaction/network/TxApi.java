package com.bitbill.www.model.transaction.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.transaction.network.entity.GetTxElement;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.transaction.network.entity.GetTxListRequest;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionResponse;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/10.
 */

public interface TxApi extends Api {

    /**
     * 获取交易相关元素
     *
     * @param getTxElement
     * @return
     */
    Observable<ApiResponse<GetTxElementResponse>> getTxElement(GetTxElement getTxElement);

    /**
     * 发送交易
     *
     * @param sendTransactionRequest
     * @return
     */
    Observable<ApiResponse<SendTransactionResponse>> sendTransaction(SendTransactionRequest sendTransactionRequest);

    /**
     * 交易记录
     *
     * @param getTxListRequest
     * @return
     */
    Observable<ApiResponse<ListTxElementResponse>> getTxList(GetTxListRequest getTxListRequest);

    /**
     * 未确认交易列表
     *
     * @param listUnconfirmRequest
     * @return
     */
    Observable<ApiResponse<ListTxElementResponse>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest);

    /**
     * 获取交易详情
     *
     * @param getTxInfoRequest
     * @return
     */
    Observable<ApiResponse<TxElement>> getTxInfo(GetTxInfoRequest getTxInfoRequest);

}
