package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
public interface AddContactByIdMvpPresenter<M extends WalletModel, V extends AddContactByIdMvpView> extends MvpPresenter<V> {

    void searchWalletId();

}
