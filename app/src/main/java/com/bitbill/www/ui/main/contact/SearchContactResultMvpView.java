package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface SearchContactResultMvpView extends MvpView {

    String getWalletId();

    String getAddress();

    String getRemark();

    String getContactName();

    void addContactSuccess();

    void addContactFail(String message);

    void requireWalletId();

    void requireContactName();

    void isExsistContact();
}
