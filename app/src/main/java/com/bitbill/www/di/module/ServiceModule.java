/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.module;

import android.app.Service;
import android.content.Context;

import com.bitbill.www.common.presenter.BtcTxMvpPresenter;
import com.bitbill.www.common.presenter.BtcTxMvpView;
import com.bitbill.www.common.presenter.BtcTxPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpView;
import com.bitbill.www.common.presenter.GetCacheVersionPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.common.presenter.GetExchangeRatePresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.presenter.ParseTxInfoPresenter;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.common.presenter.SyncAddressPresenter;
import com.bitbill.www.di.qualifier.ServiceContext;
import com.bitbill.www.di.scope.PerService;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.wallet.WalletModel;

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

    @Provides
    @PerService
    GetCacheVersionMvpPresenter<WalletModel, GetCacheVersionMvpView> provideGetCacheVersionPresenter(
            GetCacheVersionPresenter<WalletModel, GetCacheVersionMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerService
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> provideParseTxInfoPresenter(
            ParseTxInfoPresenter<TxModel, ParseTxInfoMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerService
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> provideSyncAddressPresenter(
            SyncAddressPresenter<AddressModel, SyncAddressMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerService
    BtcTxMvpPresenter<TxModel, BtcTxMvpView> provideBtcTxPresenter(
            BtcTxPresenter<TxModel, BtcTxMvpView> presenter) {
        return presenter;
    }

}
