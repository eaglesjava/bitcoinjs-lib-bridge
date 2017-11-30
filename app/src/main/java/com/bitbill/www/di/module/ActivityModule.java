/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.bitbill.www.common.rx.AppSchedulerProvider;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.qualifier.ActivityContext;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.ui.main.AssetMvpPresenter;
import com.bitbill.www.ui.main.AssetMvpView;
import com.bitbill.www.ui.main.AssetPresenter;
import com.bitbill.www.ui.main.MainMvpPresenter;
import com.bitbill.www.ui.main.MainMvpView;
import com.bitbill.www.ui.main.MainPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmMvpPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmMvpView;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletMvpPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletMvpView;
import com.bitbill.www.ui.wallet.backup.BackupWalletPresenter;
import com.bitbill.www.ui.wallet.importing.ImportWalletMvpPresenter;
import com.bitbill.www.ui.wallet.importing.ImportWalletMvpView;
import com.bitbill.www.ui.wallet.importing.ImportWalletPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletMvpPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletMvpView;
import com.bitbill.www.ui.wallet.init.InitWalletPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletSuccessMvpPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletSuccessMvpView;
import com.bitbill.www.ui.wallet.init.InitWalletSuccessPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    MainMvpPresenter<AppModel, MainMvpView> provideMainPresenter(
            MainPresenter<AppModel, MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    InitWalletMvpPresenter<WalletModel, InitWalletMvpView> provideInitWalletPresenter(
            InitWalletPresenter<WalletModel, InitWalletMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    InitWalletSuccessMvpPresenter<WalletModel, InitWalletSuccessMvpView> provideInitWalletSuccessPresenter(
            InitWalletSuccessPresenter<WalletModel, InitWalletSuccessMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ImportWalletMvpPresenter<WalletModel, ImportWalletMvpView> provideImportWalletPresenter(
            ImportWalletPresenter<WalletModel, ImportWalletMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BackupWalletMvpPresenter<WalletModel, BackupWalletMvpView> provideBackupWalletPresenter(
            BackupWalletPresenter<WalletModel, BackupWalletMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BackupWalletConfirmMvpPresenter<WalletModel, BackupWalletConfirmMvpView> provideBackupWalletConfirmPresenter(
            BackupWalletConfirmPresenter<WalletModel, BackupWalletConfirmMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    AssetMvpPresenter<WalletModel, AssetMvpView> provideAssetPresenter(
            AssetPresenter<WalletModel, AssetMvpView> presenter) {
        return presenter;
    }

}
