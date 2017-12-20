/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.component;

import com.bitbill.www.di.module.ActivityModule;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.main.asset.AssetFragment;
import com.bitbill.www.ui.main.receive.BtcReceiveFragment;
import com.bitbill.www.ui.main.receive.ReceiveFragment;
import com.bitbill.www.ui.main.send.BtcSendFragment;
import com.bitbill.www.ui.main.send.SendConfirmActivity;
import com.bitbill.www.ui.splash.SplashActivity;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.backup.BackupWalletConfirmActivity;
import com.bitbill.www.ui.wallet.importing.ImportWalletActivity;
import com.bitbill.www.ui.wallet.init.CreateWalletIdActivity;
import com.bitbill.www.ui.wallet.init.InitWalletActivity;
import com.bitbill.www.ui.wallet.init.ResetPwdActivity;

import dagger.Component;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(InitWalletActivity initWalletActivity);

    void inject(ImportWalletActivity importWalletActivity);

    void inject(BackUpWalletActivity backUpWalletActivity);

    void inject(BackupWalletConfirmActivity backupWalletConfirmActivity);

    void inject(AssetFragment assetFragment);

    void inject(GuideActivity guideActivity);

    void inject(SplashActivity splashActivity);

    void inject(ReceiveFragment receiveFragment);

    void inject(CreateWalletIdActivity createWalletIdActivity);

    void inject(ResetPwdActivity resetPwdActivity);

    void inject(BtcReceiveFragment btcReceiveFragment);

    void inject(SendConfirmActivity sendConfirmActivity);

    void inject(BtcSendFragment btcSendFragment);
}
