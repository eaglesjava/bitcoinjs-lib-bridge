/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.bitbill.www.common.presenter.BtcAddressMvpPresentder;
import com.bitbill.www.common.presenter.BtcAddressMvpView;
import com.bitbill.www.common.presenter.BtcAddressPresenter;
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
import com.bitbill.www.common.presenter.ValidateAddressMvpPresenter;
import com.bitbill.www.common.presenter.ValidateAddressMvpView;
import com.bitbill.www.common.presenter.ValidateAddressPresenter;
import com.bitbill.www.common.presenter.WalletMvpPresenter;
import com.bitbill.www.common.presenter.WalletMvpView;
import com.bitbill.www.common.presenter.WalletPresenter;
import com.bitbill.www.common.rx.AppSchedulerProvider;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.qualifier.ActivityContext;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.ui.guide.GuideMvpPresenter;
import com.bitbill.www.ui.guide.GuideMvpView;
import com.bitbill.www.ui.guide.GuidePresenter;
import com.bitbill.www.ui.main.MainMvpPresenter;
import com.bitbill.www.ui.main.MainMvpView;
import com.bitbill.www.ui.main.MainPresenter;
import com.bitbill.www.ui.main.contact.AddContactByAddressMvpPresenter;
import com.bitbill.www.ui.main.contact.AddContactByAddressMvpView;
import com.bitbill.www.ui.main.contact.AddContactByAddressPresenter;
import com.bitbill.www.ui.main.contact.AddContactByIdMvpPresenter;
import com.bitbill.www.ui.main.contact.AddContactByIdMvpView;
import com.bitbill.www.ui.main.contact.AddContactByIdPresenter;
import com.bitbill.www.ui.main.contact.ContactMvpPresenter;
import com.bitbill.www.ui.main.contact.ContactMvpView;
import com.bitbill.www.ui.main.contact.ContactPresenter;
import com.bitbill.www.ui.main.contact.EditContactMvpPresenter;
import com.bitbill.www.ui.main.contact.EditContactMvpView;
import com.bitbill.www.ui.main.contact.EditContactPresenter;
import com.bitbill.www.ui.main.contact.SearchContactResultMvpPresenter;
import com.bitbill.www.ui.main.contact.SearchContactResultMvpView;
import com.bitbill.www.ui.main.contact.SearchContactResultPresenter;
import com.bitbill.www.ui.main.my.ContactSettingMvpPresenter;
import com.bitbill.www.ui.main.my.ContactSettingMvpView;
import com.bitbill.www.ui.main.my.ContactSettingPresenter;
import com.bitbill.www.ui.main.my.ShortCutSettingMvpPresenter;
import com.bitbill.www.ui.main.my.ShortCutSettingMvpView;
import com.bitbill.www.ui.main.my.ShortCutSettingPresenter;
import com.bitbill.www.ui.main.my.SystemSettingMvpPresenter;
import com.bitbill.www.ui.main.my.SystemSettingMvpView;
import com.bitbill.www.ui.main.my.SystemSettingPresenter;
import com.bitbill.www.ui.main.my.WalletAddressMvpPresenter;
import com.bitbill.www.ui.main.my.WalletAddressMvpView;
import com.bitbill.www.ui.main.my.WalletAddressPresenter;
import com.bitbill.www.ui.main.my.WalletDetailMvpPresenter;
import com.bitbill.www.ui.main.my.WalletDetailMvpView;
import com.bitbill.www.ui.main.my.WalletDetailPresenter;
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
import com.bitbill.www.ui.wallet.info.BtcRecordMvpPresenter;
import com.bitbill.www.ui.wallet.info.BtcRecordMvpView;
import com.bitbill.www.ui.wallet.info.BtcRecordPresenter;
import com.bitbill.www.ui.wallet.init.CreateWalletIdMvpPresenter;
import com.bitbill.www.ui.wallet.init.CreateWalletIdMvpView;
import com.bitbill.www.ui.wallet.init.CreateWalletIdPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletMvpPresenter;
import com.bitbill.www.ui.wallet.init.InitWalletMvpView;
import com.bitbill.www.ui.wallet.init.InitWalletPresenter;

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
    SplashMvpPresenter<AppModel, SplashMvpView> provideSplashPresenter(
            SplashPresenter<AppModel, SplashMvpView> presenter) {
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
    BtcReceiveMvpPresenter<WalletModel, BtcReceiveMvpView> provideBtcReceivePresenter(
            BtcReceivePresenter<WalletModel, BtcReceiveMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SendConfirmMvpPresenter<TxModel, SendConfirmMvpView> provideSendConfrimPresenter(
            SendConfirmPresenter<TxModel, SendConfirmMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BtcAddressMvpPresentder<AddressModel, BtcAddressMvpView> provideBtcAddressPresenter(
            BtcAddressPresenter<AddressModel, BtcAddressMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> provideSyncAddressPresenter(
            SyncAddressPresenter<AddressModel, SyncAddressMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BtcSendMvpPresenter<ContactModel, BtcSendMvpView> provideBtcSendPresenter(
            BtcSendPresenter<ContactModel, BtcSendMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    WalletDetailMvpPresenter<WalletModel, WalletDetailMvpView> provideWalletDetailPresenter(
            WalletDetailPresenter<WalletModel, WalletDetailMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    WalletMvpPresenter<WalletModel, WalletMvpView> provideWalletPresenter(
            WalletPresenter<WalletModel, WalletMvpView> presenter) {
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
    ValidateAddressMvpPresenter<AddressModel, ValidateAddressMvpView> provideGetLastAddressPresenter(
            ValidateAddressPresenter<AddressModel, ValidateAddressMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    AddContactByAddressMvpPresenter<ContactModel, AddContactByAddressMvpView> provideAddContactByAddressPresenter(
            AddContactByAddressPresenter<ContactModel, AddContactByAddressMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ContactSettingMvpPresenter<ContactModel, ContactSettingMvpView> provideContactSettingPresenter(
            ContactSettingPresenter<ContactModel, ContactSettingMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ShortCutSettingMvpPresenter<AppModel, ShortCutSettingMvpView> provideShortCutSettingPresenter(
            ShortCutSettingPresenter<AppModel, ShortCutSettingMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    EditContactMvpPresenter<ContactModel, EditContactMvpView> provideEditContactPresenter(
            EditContactPresenter<ContactModel, EditContactMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SystemSettingMvpPresenter<AppModel, SystemSettingMvpView> provideSystemSettingPresenter(
            SystemSettingPresenter<AppModel, SystemSettingMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> provideGetExchangeRatePresenter(
            GetExchangeRatePresenter<AppModel, GetExchangeRateMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GetCacheVersionMvpPresenter<WalletModel, GetCacheVersionMvpView> provideGetCacheVersionPresenter(
            GetCacheVersionPresenter<WalletModel, GetCacheVersionMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> provideParseTxInfoPresenter(
            ParseTxInfoPresenter<TxModel, ParseTxInfoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BtcRecordMvpPresenter<TxModel, BtcRecordMvpView> provideBtcRecordPresenter(
            BtcRecordPresenter<TxModel, BtcRecordMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    WalletAddressMvpPresenter<TxModel, WalletAddressMvpView> provideWalletAddressPresenter(
            WalletAddressPresenter<TxModel, WalletAddressMvpView> presenter) {
        return presenter;
    }

}
