/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.db;


import com.bitbill.www.model.wallet.db.entity.DaoMaster;
import com.bitbill.www.model.wallet.db.entity.DaoSession;

import javax.inject.Inject;

/**
 * Created by isanwenyu@163.com on 2017/7/26.
 */
public class DbHelper implements Db {

    protected final DaoSession mDaoSession;

    @Inject
    public DbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }
}
