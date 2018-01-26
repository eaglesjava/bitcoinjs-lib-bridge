package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
public interface AddContactByIdMvpView extends MvpView {
    String getWalletId();

    void searchWalletIdSuccess();

    void searchWalletIdFail();

    void requireWalletId();
}
