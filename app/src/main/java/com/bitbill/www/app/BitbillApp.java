package com.bitbill.www.app;

import android.app.Application;

import com.bitbill.www.utils.BitcoinJsWrapper;

/**
 * Created by zhuyuanbao on 2017/11/7.
 */
public class BitbillApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BitcoinJsWrapper.init(this);
    }
}
