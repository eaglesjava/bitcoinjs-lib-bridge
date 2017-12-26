package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.contact.ContactModel;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface ContactMvpPresenter<M extends ContactModel, V extends ContactMvpView> extends MvpPresenter<V> {
    void loadContact();
}
