package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.contact.ContactModel;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface GetLastAddressMvpPresenter<M extends ContactModel, V extends GetLastAddressMvpView> extends MvpPresenter<V> {


    void getLastAddress();
}
