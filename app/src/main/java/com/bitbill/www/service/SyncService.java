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
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.model.app.AppModel;
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
public class SyncService extends BaseService<GetExchangeRateMvpPresenter> implements GetExchangeRateMvpView {

    private final String TAG = "SyncService";

    @Inject
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> mGetExchangeRateMvpPresenter;
    private volatile boolean isAppBackground;

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncService.class);
        context.startService(starter);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        //每30秒刷新汇率
        refreshExchangeRate();
        EventBus.getDefault().register(this);
    }

    private void refreshExchangeRate() {
        getCompositeDisposable().add(Observable.interval(0, 30, TimeUnit.SECONDS, Schedulers.trampoline())
                .filter(aLong -> !isAppBackground)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
                        getMvpPresenter().getExchangeRate();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }))
        ;
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

    @Override
    public void getBtcRateSuccess(double cnyRate, double usdRate) {

        //通知界面已更新
        EventBus.getDefault().post(new RefreshExchangeRateEvent(cnyRate, usdRate));
    }
}
