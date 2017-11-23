package com.bitbill.www.ui.wallet.importing;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
@PerActivity
public interface ImportWalletMvpPresenter<M extends WalletModel, V extends ImportWalletMvpView> extends MvpPresenter<V> {
    /**
     * import a wallet by the mnemonic
     */
    void importWallet();
}
