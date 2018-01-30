package com.bitbill.www.model.transaction;

import android.content.Context;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.transaction.db.TxDb;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.TxApi;
import com.bitbill.www.model.transaction.network.entity.GetTxElement;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.transaction.network.entity.GetTxListRequest;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionResponse;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/10.
 */

public class TxModelManager extends ModelManager implements TxModel {

    private final TxDb mTxDb;
    private final Context mContext;
    private final TxApi mTxApi;

    @Inject
    public TxModelManager(@ApplicationContext Context context, TxDb txDb, TxApi txApi) {
        mContext = context;
        mTxDb = txDb;
        mTxApi = txApi;
    }

    @Override
    public Observable<ApiResponse<GetTxElementResponse>> getTxElement(GetTxElement getTxElement) {
        return mTxApi.getTxElement(getTxElement);
    }

    @Override
    public Observable<ApiResponse<SendTransactionResponse>> sendTransaction(SendTransactionRequest sendTransactionRequest) {
        return mTxApi.sendTransaction(sendTransactionRequest);
    }

    /**
     * 交易记录
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> getTxList(GetTxListRequest getTxListRequest) {
        return mTxApi.getTxList(getTxListRequest);
    }

    /**
     * 未确认交易列表
     *
     * @param listUnconfirmRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest) {
        return mTxApi.listUnconfirm(listUnconfirmRequest);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mTxApi.getApiHeader();
    }

    @Override
    public Long insertTxRecordAndInputsOutputs(TxRecord txRecord, List<TxElement.InputsBean> inputs, List<TxElement.OutputsBean> outputs) {
        return mTxDb.insertTxRecordAndInputsOutputs(txRecord, inputs, outputs);
    }

    @Override
    public Observable<List<TxRecord>> getTxRecords() {
        return mTxDb.getTxRecords();
    }

    @Override
    public Observable<List<TxRecord>> getUnConfirmedTxRecord() {
        return mTxDb.getUnConfirmedTxRecord();
    }

    @Override
    public Observable<ApiResponse<TxElement>> getTxInfo(GetTxInfoRequest getTxInfoRequest) {
        return mTxApi.getTxInfo(getTxInfoRequest);
    }

}
