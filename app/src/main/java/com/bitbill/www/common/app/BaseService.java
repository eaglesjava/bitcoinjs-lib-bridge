package com.bitbill.www.common.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.component.DaggerServiceComponent;
import com.bitbill.www.di.component.ServiceComponent;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2018/3/28.
 */

public class BaseService extends Service {
    @Inject
    CompositeDisposable mCompositeDisposable;
    @Inject
    SchedulerProvider mSchedulerProvider;
    private BitbillApp mBitbillApp;
    private ServiceComponent mServiceComponent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBitbillApp = BitbillApp.get();
        mServiceComponent = DaggerServiceComponent.builder().applicationComponent(mBitbillApp.getComponent())
                .build();
    }

    public BitbillApp getApp() {
        return mBitbillApp;
    }

    public ServiceComponent getServiceComponent() {
        return mServiceComponent;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getCompositeDisposable().dispose();
    }

    /**
     * apply to changing the loading state of mvpview & scheduler
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applyScheduler() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(getSchedulerProvider().io())
                        .subscribeOn(getSchedulerProvider().ui())
                        .observeOn(getSchedulerProvider().ui());
            }
        };
    }

}
