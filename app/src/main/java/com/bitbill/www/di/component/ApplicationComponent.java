/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.component;

import android.app.Application;
import android.content.Context;

import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.presenter.GetExchangeRateMvpPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.module.ApplicationModule;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.wallet.WalletModel;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.disposables.CompositeDisposable;
import io.socket.client.Socket;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BitbillApp app);

    @ApplicationContext
    Context context();

    Application application();

    CompositeDisposable getDisposable();

    SchedulerProvider getScheduler();

    AppModel getAppModel();

    WalletModel getWalletModel();

    ContactModel getContactModel();

    AddressModel getAddressModel();

    TxModel getTxModel();

    Socket getSocket();

    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> getExchangeRateMvpPresenter();
}