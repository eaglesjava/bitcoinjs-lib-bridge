/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.di.component;

import com.bitbill.www.di.module.ActivityModule;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.wallet.InitWalletActivity;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.importing.ImportWalletActivity;

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
}
