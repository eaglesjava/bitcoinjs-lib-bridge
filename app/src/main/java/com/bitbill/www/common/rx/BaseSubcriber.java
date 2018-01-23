/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.rx;


import com.bitbill.www.R;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.common.utils.StringUtils;

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
        } else {
            return;
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
        if (t instanceof ApiResponse) {
            if (handleApiResponse((ApiResponse) t)) {
                return;
            }
        }
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
        } else {
            return;
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
        } else {
            return;
        }
    }

    public boolean isValidMvpView() {
        return mMvpView != null;
    }

    public MvpView getSubcriberMvpView() {
        return mMvpView;
    }

    public boolean handleApiResponse(ApiResponse apiResponse) {
        if (!isValidMvpView()) {
            return true;
        }

        if (apiResponse == null) {
            getSubcriberMvpView().onError(R.string.error_api_server);
            return true;
        }
        if (apiResponse.isSuccess()) {
            return false;
        }
        switch (apiResponse.getStatus()) {
            case ApiResponse.STATUS_SERVER_BUSY:
                getSubcriberMvpView().onError(R.string.error_server_busy);
                return true;
            case ApiResponse.STATUS_LACK_MADATORY_PARAMS:
                getSubcriberMvpView().onError(R.string.error_lack_madatory_params);
                return true;
            case ApiResponse.STATUS_INVALID_PARAM_TYPE:
                getSubcriberMvpView().onError(R.string.error_invalid_param_type);
                return true;
//            case ApiResponse.STATUS_WALLET_ID_EXSIST:
//                getSubcriberMvpView().onError(R.string.error_wallet_id_exsist);
//                return true;
            case ApiResponse.STATUS_WALLET_NO_EXSIST:
                getSubcriberMvpView().onError(R.string.error_wallet_no_exsist);
                return true;

        }
        if (StringUtils.isNotEmpty(apiResponse.getMessage())) {
            getSubcriberMvpView().onError(apiResponse.getMessage());
            return true;
        } else {
            getSubcriberMvpView().onError(R.string.error_api_default);
            return true;
        }
    }
}
