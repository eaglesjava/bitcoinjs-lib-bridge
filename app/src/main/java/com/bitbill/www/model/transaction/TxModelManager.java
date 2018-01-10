package com.bitbill.www.model.transaction;

import android.content.Context;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.transaction.db.TxDb;
import com.bitbill.www.model.transaction.db.entity.Input;
import com.bitbill.www.model.transaction.db.entity.Output;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.TxApi;
import com.bitbill.www.model.transaction.network.entity.GetTxElement;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.GetTxListRequest;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionResponse;

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
    public Long insertTxRecord(TxRecord txRecord) {
        return mTxDb.insertTxRecord(txRecord);
    }

    @Override
    public Long insertInput(Input input) {
        return mTxDb.insertInput(input);
    }

    @Override
    public Long insertOutput(Output output) {
        return mTxDb.insertOutput(output);
    }
}
