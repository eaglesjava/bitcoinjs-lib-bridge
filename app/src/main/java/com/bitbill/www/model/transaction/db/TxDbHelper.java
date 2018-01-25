package com.bitbill.www.model.transaction.db;

import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.model.db.dao.InputDao;
import com.bitbill.model.db.dao.OutputDao;
import com.bitbill.model.db.dao.TxRecordDao;
import com.bitbill.www.common.base.model.db.DbHelper;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.transaction.db.entity.Input;
import com.bitbill.www.model.transaction.db.entity.Output;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/10.
 */

@Singleton
public class TxDbHelper extends DbHelper implements TxDb {

    private final TxRecordDao mTxRecordDao;
    private final InputDao mInputDao;
    private final OutputDao mOutputDao;

    @Inject
    public TxDbHelper(@DatabaseInfo DaoSession daoSession) {
        super(daoSession);
        mTxRecordDao = daoSession.getTxRecordDao();
        mInputDao = daoSession.getInputDao();
        mOutputDao = daoSession.getOutputDao();
    }

    @Override
    public Long insertTxRecordAndInputsOutputs(TxRecord txRecord, List<TxElement.InputsBean> inputs, List<TxElement.OutputsBean> outputs) {

        return getDaoSession().callInTxNoException(() -> {
            long rowId = mTxRecordDao.insertOrReplace(txRecord);
            mOutputDao.insertOrReplaceInTx(getOutputs(outputs, txRecord));
            mInputDao.insertOrReplaceInTx(getInputs(inputs, txRecord));
            return rowId;
        });
    }

    @Override
    public Observable<List<TxRecord>> getTxRecords() {
        return Observable.fromCallable(() -> mTxRecordDao.loadAll());
    }

    @Override
    public Observable<List<TxRecord>> getUnConfirmedTxRecord() {
        return Observable.fromCallable(() -> mTxRecordDao.queryBuilder().where(TxRecordDao.Properties.Height.eq(-1)).orderDesc(TxRecordDao.Properties.CreatedTime).list());
    }

    private List<Output> getOutputs(List<TxElement.OutputsBean> outputs, TxRecord txRecord) {
        List<Output> oOutputs = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            TxElement.OutputsBean outputsBean = outputs.get(i);
            Output output = new Output();
            output.setAddress(outputsBean.getAddress());
            output.setValue(outputsBean.getValue());
            output.setTxId(txRecord.getId());
            output.setWalletId(txRecord.getWalletId());
            output.setTxHash(txRecord.getTxHash());
            output.setTxIndex(i);
            oOutputs.add(output);
        }
        return oOutputs;
    }

    private List<Input> getInputs(List<TxElement.InputsBean> inputs, TxRecord txRecord) {
        List<Input> nInputs = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            TxElement.InputsBean inputsBean = inputs.get(i);
            Input input = new Input();
            input.setTxId(txRecord.getId());
            input.setWalletId(txRecord.getWalletId());
            input.setAddress(inputsBean.getAddress());
            input.setValue(inputsBean.getValue());
            input.setTxHash(txRecord.getTxHash());
            input.setTxIndex(i);
            nInputs.add(input);
        }
        return nInputs;
    }

}
