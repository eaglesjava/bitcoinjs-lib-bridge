package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu on 2018/2/5.
 */

public interface BalanceMvpPresenter<M extends WalletModel, V extends BalanceMvpView> extends MvpPresenter<V> {
    void getBalance();

    void loadBalance();

}
