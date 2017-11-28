/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.view;

import android.support.annotation.StringRes;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpView {

    void showLoading();

    void hideLoading();

    void onTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();
}
