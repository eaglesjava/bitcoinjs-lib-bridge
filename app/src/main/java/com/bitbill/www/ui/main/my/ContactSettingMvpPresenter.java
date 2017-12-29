package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.contact.ContactModel;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public interface ContactSettingMvpPresenter<M extends ContactModel, V extends MvpView> extends MvpPresenter<V> {

    String getWalletKey();

    void recoverContact(String contactKey);
}
