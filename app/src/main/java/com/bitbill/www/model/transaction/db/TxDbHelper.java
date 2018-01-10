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

import javax.inject.Inject;
import javax.inject.Singleton;

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
    public Long insertTxRecord(TxRecord txRecord) {
        return mTxRecordDao.insertOrReplace(txRecord);
    }

    @Override
    public Long insertInput(Input input) {
        return mInputDao.insert(input);
    }

    @Override
    public Long insertOutput(Output output) {
        return mOutputDao.insert(output);
    }
}
