package com.bitbill.www.ui.main.send;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.contact.ContactModel;

/**
 * Created by isanwenyu on 2017/12/20.
 */

public interface BtcSendMvpPresenter<M extends ContactModel, V extends BtcSendMvpView> extends MvpPresenter<V> {

    void getLastAddress();

    void updateContact();
}
