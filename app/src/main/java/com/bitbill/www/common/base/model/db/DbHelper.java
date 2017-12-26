/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.db;


import com.bitbill.www.model.contact.db.entity.DaoSession;

/**
 * Created by isanwenyu@163.com on 2017/7/26.
 */
public class DbHelper implements Db {

    protected final DaoSession mDaoSession;

    public DbHelper(DaoSession daoSession) {
        mDaoSession = daoSession;
    }
}
