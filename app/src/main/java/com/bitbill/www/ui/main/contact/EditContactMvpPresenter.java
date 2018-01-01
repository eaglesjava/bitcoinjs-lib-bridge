package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.contact.ContactModel;

/**
 * Created by isanwenyu@163.com on 2018/1/1.
 */
public interface EditContactMvpPresenter<M extends ContactModel, V extends EditContactMvpView> extends MvpPresenter<V> {

    void updateContact();

    void deleteContact();

}
