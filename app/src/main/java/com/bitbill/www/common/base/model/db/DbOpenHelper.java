/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.db;

import android.content.Context;
import android.util.Log;

import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.app.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
        switch (oldVersion) {
            case 1:
            case 2:
                //db.execSQL("ALTER TABLE " + AppDao.TABLENAME + " ADD COLUMN "
                // + AppDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
        }
    }
}
