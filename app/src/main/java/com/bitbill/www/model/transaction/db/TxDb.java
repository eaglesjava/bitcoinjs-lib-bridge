package com.bitbill.www.model.transaction.db;

import com.bitbill.www.common.base.model.db.Db;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by isanwenyu on 2018/1/10.
 */

public interface TxDb extends Db {

    Long insertTxRecordAndInputsOutputs(TxRecord txRecord, List<TxElement.InputsBean> inputs, List<TxElement.OutputsBean> outputs);

    Observable<List<TxRecord>> getTxRecords();
}
