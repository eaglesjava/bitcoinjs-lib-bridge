package com.bitbill.www.model.transaction;

import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.model.transaction.db.TxDb;
import com.bitbill.www.model.transaction.network.TxApi;

/**
 * Created by isanwenyu on 2018/1/10.
 */

public interface TxModel extends Model, TxApi, TxDb {
}
