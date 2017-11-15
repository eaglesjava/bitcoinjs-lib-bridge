package com.bitbill.www.common.app;

import android.app.Application;

import com.bitbill.www.utils.BitcoinJsWrapper;

/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitbillApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BitcoinJsWrapper.getInstance().init(this);
    }
}
