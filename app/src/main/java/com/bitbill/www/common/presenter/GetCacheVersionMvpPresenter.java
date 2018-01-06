package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface GetCacheVersionMvpPresenter<M extends WalletModel, V extends MvpView> extends MvpPresenter<V> {

    void getCacheVersion();
}
