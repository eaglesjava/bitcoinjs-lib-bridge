package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public interface ContactSettingMvpView extends MvpView {

    void requireContactKey();

    void recoverContactSuccess();

    void receoverContactFail();

    void receoverContactsNull();
}
