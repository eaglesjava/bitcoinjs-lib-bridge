package com.bitbill.www.ui.wallet.backup;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/11/23.
 */
@PerActivity
public interface BackupWalletMvpPresenter<W extends WalletModel, V extends BackupWalletMvpView> extends MvpPresenter<V> {

    void loadMnemonic(String confirmPwd);
}
