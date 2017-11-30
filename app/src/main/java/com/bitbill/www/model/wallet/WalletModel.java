/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.wallet;

import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.model.wallet.db.WalletDb;
import com.bitbill.www.model.wallet.network.WalletApi;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public interface WalletModel extends Model, WalletDb, WalletApi {

}
