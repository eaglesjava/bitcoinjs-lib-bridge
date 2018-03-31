/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.module;

import android.app.Service;
import android.content.Context;

import com.bitbill.www.common.presenter.GetExchangeRateMvpPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.common.presenter.GetExchangeRatePresenter;
import com.bitbill.www.di.qualifier.ServiceContext;
import com.bitbill.www.di.scope.PerService;
import com.bitbill.www.model.app.AppModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Module
public class ServiceModule {

    private final Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @ServiceContext
    Context provideContext() {
        return mService;
    }

    @Provides
    @PerService
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> provideGetExchangeRatePresenter(
            GetExchangeRatePresenter<AppModel, GetExchangeRateMvpView> presenter) {
        return presenter;
    }

}
