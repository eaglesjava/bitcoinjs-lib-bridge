package com.bitbill.www.model.btc;

import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.model.btc.db.BtcDb;
import com.bitbill.www.model.btc.network.BtcApi;

/**
 * Created by isanwenyu on 2018/1/10.
 */

public interface BtcModel extends Model, BtcApi, BtcDb {
}
