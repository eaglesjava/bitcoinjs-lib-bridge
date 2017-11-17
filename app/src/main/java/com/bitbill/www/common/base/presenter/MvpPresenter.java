/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.presenter;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

import com.bitbill.www.common.base.view.MvpView;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

}
