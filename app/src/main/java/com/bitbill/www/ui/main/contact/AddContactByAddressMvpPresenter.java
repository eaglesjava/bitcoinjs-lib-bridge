package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.contact.ContactModel;

/**
 * Created by isanwenyu on 2017/12/27.
 */

public interface AddContactByAddressMvpPresenter<M extends ContactModel, V extends AddContactByAddressMvpView> extends MvpPresenter<V> {

    void checkContact();

    void addContact();
}
