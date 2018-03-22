package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface ValidateAddressMvpPresenter<M extends BtcModel, V extends ValidateAddressMvpView> extends MvpPresenter<V> {


    void validateBtcAddress();
}
