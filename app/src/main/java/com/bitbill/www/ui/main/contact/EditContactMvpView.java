package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.contact.db.entity.Contact;

/**
 * Created by isanwenyu@163.com on 2018/1/1.
 */
public interface EditContactMvpView extends MvpView {
    Contact getContact();

    void updateContactSuccess();

    void updateContactFail(String message);

    String getContactName();

    String getRemark();

    void requireContactName();

    void requireContact();

    void contactNoEdit();

    void deleteContactSuccess();

    void deleteContactFail(String message);
}
