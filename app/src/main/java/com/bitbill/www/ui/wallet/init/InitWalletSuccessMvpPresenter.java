package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public interface InitWalletSuccessMvpPresenter<M extends WalletModel, V extends InitWalletSuccessMvpView> extends MvpPresenter<V> {

    void createWallet();
}
