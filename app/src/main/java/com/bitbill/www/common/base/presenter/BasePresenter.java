/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.presenter;

/**
 * Created by isanwenyu@163.com on 27/01/17.
 */

import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.common.rx.SchedulerProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    private BitbillApp mApp;

    public BasePresenter(SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;

        mApp = BitbillApp.get();
    }

    public BitbillApp getApp() {
        return mApp;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    /**
     * apply to changing the loading state of mvpview & scheduler
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applyScheduler() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(getSchedulerProvider().io())
                        .subscribeOn(getSchedulerProvider().ui())
                        .observeOn(getSchedulerProvider().ui());
            }
        };
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
