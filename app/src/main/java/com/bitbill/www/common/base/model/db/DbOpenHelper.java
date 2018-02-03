/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.db;

import android.content.Context;
import android.util.Log;

import com.bitbill.model.db.dao.AddressDao;
import com.bitbill.model.db.dao.ContactDao;
import com.bitbill.model.db.dao.DaoMaster;
import com.bitbill.model.db.dao.InputDao;
import com.bitbill.model.db.dao.OutputDao;
import com.bitbill.model.db.dao.TxRecordDao;
import com.bitbill.model.db.dao.WalletDao;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {
    private static final String TAG = "DbOpenHelper";

    @Inject
    public DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d(TAG, "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, WalletDao.class, ContactDao.class, AddressDao.class, TxRecordDao.class, InputDao.class, OutputDao.class);
    }
}
