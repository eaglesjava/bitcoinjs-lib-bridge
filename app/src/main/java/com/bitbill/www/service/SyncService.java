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
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.network.entity.GetExchangeRateResponse;
import com.bitbill.www.model.eventbus.AppBackgroundEvent;
import com.bitbill.www.model.eventbus.RefreshExchangeRateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */
public class SyncService extends BaseService {

    private final String TAG = "SyncService";

    @Inject
    AppModel mAppModel;
    private volatile boolean isAppBackground;

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncService.class);
        context.startService(starter);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        getServiceComponent().inject(this);
        //每30秒刷新汇率
        refreshExchangeRate();
        EventBus.getDefault().register(this);
    }

    private void refreshExchangeRate() {
        getCompositeDisposable().add(
                Observable.interval(0, 30, TimeUnit.SECONDS, Schedulers.trampoline())
                        .filter(aLong -> !isAppBackground)
                        .concatMap(aLong -> mAppModel.getExchangeRate())
                        .compose(this.applyScheduler())
                        .subscribeWith(new BaseSubcriber<ApiResponse<GetExchangeRateResponse>>() {

                            @Override
                            public void onNext(ApiResponse<GetExchangeRateResponse> getExchangeRateResponseApiResponse) {
                                super.onNext(getExchangeRateResponseApiResponse);
                                Log.d(TAG, "onNext() called with: getExchangeRateResponseApiResponse = [" + getExchangeRateResponseApiResponse + "]");
                                if (getExchangeRateResponseApiResponse != null) {
                                    if (getExchangeRateResponseApiResponse.isSuccess()) {
                                        GetExchangeRateResponse data = getExchangeRateResponseApiResponse.getData();
                                        if (data != null) {
                                            getApp().setBtcCnyValue(data.getCnyrate());
                                            getApp().setBtcUsdValue(data.getUsdrate());
                                            //通知界面已更新
                                            EventBus.getDefault().post(new RefreshExchangeRateEvent(data.getCnyrate(), data.getUsdrate()));
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        }));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppBackgroundEvent(AppBackgroundEvent appBackgroundEvent) {
        isAppBackground = appBackgroundEvent.isBackground();
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
}
