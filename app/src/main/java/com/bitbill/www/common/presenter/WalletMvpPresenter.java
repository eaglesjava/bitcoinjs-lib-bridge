package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu on 2017/12/28.
 */

public interface WalletMvpPresenter<M extends WalletModel, V extends WalletMvpView> extends MvpPresenter<V> {

    void loadWallets();
}
