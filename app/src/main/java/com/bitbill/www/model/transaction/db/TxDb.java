package com.bitbill.www.model.transaction.db;

import com.bitbill.www.common.base.model.db.Db;
import com.bitbill.www.model.transaction.db.entity.Input;
import com.bitbill.www.model.transaction.db.entity.Output;
import com.bitbill.www.model.transaction.db.entity.TxRecord;


/**
 * Created by isanwenyu on 2018/1/10.
 */

public interface TxDb extends Db {

    Long insertTxRecord(TxRecord txRecord);

    Long insertInput(Input input);

    Long insertOutput(Output output);
}
