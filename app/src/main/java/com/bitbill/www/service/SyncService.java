/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bitbill.www.common.app.BaseService;
import com.bitbill.www.common.presenter.GetExchangeRateMvpPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.eventbus.AppBackgroundEvent;
import com.bitbill.www.model.eventbus.RefreshExchangeRateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */
public class SyncService extends BaseService<GetExchangeRateMvpPresenter> implements GetExchangeRateMvpView {

    private final String TAG = "SyncService";

    @Inject
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> mGetExchangeRateMvpPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncService.class);
        context.startService(starter);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        //每30秒刷新汇率
        getMvpPresenter().refreshExchangeRate();
        EventBus.getDefault().register(this);
    }

    @Override
    public GetExchangeRateMvpPresenter getMvpPresenter() {
        return mGetExchangeRateMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getServiceComponent().inject(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppBackgroundEvent(AppBackgroundEvent appBackgroundEvent) {
        if (appBackgroundEvent == null) {
            return;
        }
        getMvpPresenter().setAppBackground(appBackgroundEvent.isBackground());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getBtcRateSuccess(double cnyRate, double usdRate) {

        //通知界面已更新
        EventBus.getDefault().post(new RefreshExchangeRateEvent(cnyRate, usdRate));
    }
}
