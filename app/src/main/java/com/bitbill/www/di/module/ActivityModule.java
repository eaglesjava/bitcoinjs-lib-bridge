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
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.ui.common.BtcAddressMvpPresentder;
import com.bitbill.www.ui.common.BtcAddressMvpView;
import com.bitbill.www.ui.common.BtcAddressPresenter;
import com.bitbill.www.ui.common.GetLastAddressMvpPresenter;
import com.bitbill.www.ui.common.GetLastAddressMvpView;
import com.bitbill.www.ui.common.GetLastAddressPresenter;
import com.bitbill.www.ui.guide.GuideMvpPresenter;
import com.bitbill.www.ui.guide.GuideMvpView;
import com.bitbill.www.ui.guide.GuidePresenter;
import com.bitbill.www.ui.main.MainMvpPresenter;
import com.bitbill.www.ui.main.MainMvpView;
import com.bitbill.www.ui.main.MainPresenter;
import com.bitbill.www.ui.main.contact.AddContactByIdMvpPresenter;
import com.bitbill.www.ui.main.contact.AddContactByIdMvpView;
import com.bitbill.www.ui.main.contact.AddContactByIdPresenter;
import com.bitbill.www.ui.main.contact.ContactMvpPresenter;
import com.bitbill.www.ui.main.contact.ContactMvpView;
import com.bitbill.www.ui.main.contact.ContactPresenter;
import com.bitbill.www.ui.main.contact.SearchContactResultMvpPresenter;
import com.bitbill.www.ui.main.contact.SearchContactResultMvpView;
import com.bitbill.www.ui.main.contact.SearchContactResultPresenter;
import com.bitbill.www.ui.main.receive.BtcReceiveMvpPresenter;
import com.bitbill.www.ui.main.receive.BtcReceiveMvpView;
import com.bitbill.www.ui.main.receive.BtcReceivePresenter;
import com.bitbill.www.ui.main.receive.ReceiveMvpPresenter;
import com.bitbill.www.ui.main.receive.ReceiveMvpView;
import com.bitbill.www.ui.main.receive.ReceivePresenter;
import com.bitbill.www.ui.main.receive.ScanPayMvpPresenter;
import com.bitbill.www.ui.main.receive.ScanPayMvpView;
import com.bitbill.www.ui.main.receive.ScanPayPresenter;
import com.bitbill.www.ui.main.send.BtcSendMvpPresenter;
import com.bitbill.www.ui.main.send.BtcSendMvpView;
import com.bitbill.www.ui.main.send.BtcSendPresenter;
import com.bitbill.www.ui.main.send.SendConfirmMvpPresenter;
import com.bitbill.www.ui.main.send.SendConfirmMvpView;
import com.bitbill.www.ui.main.send.SendConfirmPresenter;
import com.bitbill.www.ui.splash.SplashMvpPresenter;
import com.bitbill.www.ui.splash.SplashMvpView;
import com.bitbill.www.ui.splash.SplashPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmMvpPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmMvpView;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletMvpPresenter;
import com.bitbill.www.ui.wallet.backup.BackupWalletMvpView;
import com.bitbill.www.ui.wallet.backup.BackupWalletPresenter;
import com.bitbill.www.ui.wallet.importing.ImportWalletMvpPresenter;
import com.bitbill.www.ui.wallet.importing.ImportWalletMvpView;
import com.bitbill.www.ui.wallet.importing.ImportWalletPresenter;
import com.bitbill.www.ui.wallet.init.CreateWalletIdMvpPresenter;
import com.bitbill.www.ui.wallet.init.CreateWalletIdMvpView;
import com.bitbill.www.ui.wallet.init.CreateWalletIdPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletMvpPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletMvpView;
import com.bitbill.www.ui.wallet.init.InitWalletPresenter;
import com.bitbill.www.ui.wallet.init.ResetPwdMvpPresenter;
import com.bitbill.www.ui.wallet.init.ResetPwdMvpView;
import com.bitbill.www.ui.wallet.init.ResetPwdPresenter;

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
    SplashMvpPresenter<WalletModel, SplashMvpView> provideSplashPresenter(
            SplashPresenter<WalletModel, SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GuideMvpPresenter<AppModel, GuideMvpView> provideGuidePresenter(
            GuidePresenter<AppModel, GuideMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<WalletModel, MainMvpView> provideMainPresenter(
            MainPresenter<WalletModel, MainMvpView> presenter) {
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
    ReceiveMvpPresenter<AppModel, ReceiveMvpView> provideReceivePresenter(
            ReceivePresenter<AppModel, ReceiveMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CreateWalletIdMvpPresenter<WalletModel, CreateWalletIdMvpView> provideCreateWalletIdPresenter(
            CreateWalletIdPresenter<WalletModel, CreateWalletIdMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ResetPwdMvpPresenter<WalletModel, ResetPwdMvpView> provideRestPwdPresenter(
            ResetPwdPresenter<WalletModel, ResetPwdMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BtcReceiveMvpPresenter<WalletModel, BtcReceiveMvpView> provideBtcReceivePresenter(
            BtcReceivePresenter<WalletModel, BtcReceiveMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SendConfirmMvpPresenter<WalletModel, SendConfirmMvpView> provideSendConfrimPresenter(
            SendConfirmPresenter<WalletModel, SendConfirmMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BtcAddressMvpPresentder<WalletModel, BtcAddressMvpView> provideBtcAddressPresenter(
            BtcAddressPresenter<WalletModel, BtcAddressMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BtcSendMvpPresenter<WalletModel, BtcSendMvpView> provideBtcSendPresenter(
            BtcSendPresenter<WalletModel, BtcSendMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ScanPayMvpPresenter<ScanPayMvpView> provideScanPayPresenter(
            ScanPayPresenter<ScanPayMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    AddContactByIdMvpPresenter<ContactModel, AddContactByIdMvpView> provideAddContactByIdPresenter(
            AddContactByIdPresenter<ContactModel, AddContactByIdMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SearchContactResultMvpPresenter<ContactModel, SearchContactResultMvpView> provideSearchContactResultPresenter(
            SearchContactResultPresenter<ContactModel, SearchContactResultMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ContactMvpPresenter<ContactModel, ContactMvpView> provideContactPresenter(
            ContactPresenter<ContactModel, ContactMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GetLastAddressMvpPresenter<ContactModel, GetLastAddressMvpView> provideGetLastAddressPresenter(
            GetLastAddressPresenter<ContactModel, GetLastAddressMvpView> presenter) {
        return presenter;
    }

}
