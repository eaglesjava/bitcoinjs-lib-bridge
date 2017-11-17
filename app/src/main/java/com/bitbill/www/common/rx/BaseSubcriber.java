/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.rx;


import com.bitbill.www.common.base.view.MvpView;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by isanwenyu@163.com on 2017/7/27.
 */
public class BaseSubcriber<T> extends DisposableObserver<T> {
    private MvpView mMvpView;

    public BaseSubcriber() {
    }

    public BaseSubcriber(MvpView mvpView) {
        mMvpView = mvpView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isValidMvpView()) {
            mMvpView.showLoading();
        }
    }

    /**
     * Provides the Observer with a new item to observe.
     * <p>
     * The {@link Observable} may call this method 0 or more times.
     * <p>
     * The {@code Observable} will not call this method again after it calls either {@link #onComplete} or
     * {@link #onError}.
     *
     * @param t the item emitted by the Observable
     */
    @Override
    public void onNext(T t) {

    }

    /**
     * Notifies the Observer that the {@link Observable} has experienced an error condition.
     * <p>
     * If the {@link Observable} calls this method, it will not thereafter call {@link #onNext} or
     * {@link #onComplete}.
     *
     * @param e the exception encountered by the Observable
     */
    @Override
    public void onError(Throwable e) {
        if (isValidMvpView()) {
            mMvpView.hideLoading();
        }
    }

    /**
     * Notifies the Observer that the {@link Observable} has finished sending push-based notifications.
     * <p>
     * The {@link Observable} will not call this method if it calls {@link #onError}.
     */
    @Override
    public void onComplete() {
        if (isValidMvpView()) {
            mMvpView.hideLoading();
        }
    }

    public boolean isValidMvpView() {
        return mMvpView != null;
    }
}
