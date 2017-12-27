package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2017/12/27.
 */

public interface AddContactByAddressMvpView extends MvpView {
    void isExsistContact();

    String getAddress();

    String getContactName();

    void requireContactName();

    void requireAddress();

    String getRemark();

    void addContactSuccess();

    void addContactFail(String message);
}
