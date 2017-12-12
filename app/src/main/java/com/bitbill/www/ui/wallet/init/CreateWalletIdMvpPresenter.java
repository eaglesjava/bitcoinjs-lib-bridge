package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public interface CreateWalletIdMvpPresenter<M extends WalletModel, V extends MvpView> extends MvpPresenter<V> {
    void checkWalletId();
}
