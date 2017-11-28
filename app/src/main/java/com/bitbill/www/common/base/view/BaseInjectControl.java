package com.bitbill.www.common.base.view;

import com.bitbill.www.common.base.presenter.MvpPresenter;

/**
 * Created by isanwenyu@163.com on 2017/11/28.
 */
public interface BaseInjectControl<P extends MvpPresenter> {

    P getMvpPresenter();

    void injectComponent();
}
