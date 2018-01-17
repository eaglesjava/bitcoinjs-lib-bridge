package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface ValidateAddressMvpView extends MvpView {

    void validateAddress(boolean validate);

    void requireAddress();

    String getAddress();
}
