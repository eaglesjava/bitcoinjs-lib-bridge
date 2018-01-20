package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public interface CreateWalletIdMvpView extends MvpView {
    void requireWalletId();

    void invalidWalletId();

    void requireWalletIdLength();

    String getWalletId();

    void checkWalletIdSuccess();

    void hasWalletIdExsist();

    void checkWalletIdFail(String message);

    void isValidIdStart();
}
