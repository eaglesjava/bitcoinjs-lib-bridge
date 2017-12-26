package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.contact.db.entity.Contact;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface ContactMvpView extends MvpView {
    void loadContactSuccess(List<Contact> contacts);

    void loadContactFail();
}
