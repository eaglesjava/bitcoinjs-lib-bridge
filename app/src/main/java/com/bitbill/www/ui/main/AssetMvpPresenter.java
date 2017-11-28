package com.bitbill.www.ui.main;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/11/28.
 */
public interface AssetMvpPresenter<M extends WalletModel, V extends AssetMvpView> extends MvpPresenter<V> {

    void loadWallet();
}
