package com.bitbill.www.ui.wallet.backup;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
@PerActivity
public interface BackupWalletConfirmMvpPresenter<M extends WalletModel, V extends BackupWalletConfirmMvpView> extends MvpPresenter<V> {

    void checkBackup();
}
