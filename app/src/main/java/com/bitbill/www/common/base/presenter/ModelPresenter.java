/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.presenter;


import android.util.Log;

import com.androidnetworking.common.ANConstants;
import com.androidnetworking.error.ANError;
import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.common.base.model.network.api.ApiError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/7/24.
 */
public class ModelPresenter<M extends Model, V extends MvpView> extends BasePresenter<V> {
    private static final String TAG = ModelPresenter.class.getSimpleName();
    private final M modelManager;

    public ModelPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
        this.modelManager = model;
    }

    public M getModelManager() {
        return modelManager;
    }

    public boolean handleApiResponse(ApiResponse apiResponse) {
        if (!isViewAttached()) {
            return true;
        }

        if (apiResponse == null) {
            getMvpView().onError(R.string.error_api_server);
            return true;
        }
        if (apiResponse.isSuccess()) {
            return false;
        }
        switch (apiResponse.getStatus()) {
            case ApiResponse.STATUS_SERVER_BUSY:
                getMvpView().onError(R.string.error_server_busy);
                return true;
            case ApiResponse.STATUS_LACK_MADATORY_PARAMS:
                getMvpView().onError(R.string.error_lack_madatory_params);
                return true;
            case ApiResponse.STATUS_INVALID_PARAM_TYPE:
                getMvpView().onError(R.string.error_invalid_param_type);
                return true;
            case ApiResponse.STATUS_WALLET_ID_EXSIST:
                getMvpView().onError(R.string.error_wallet_id_exsist);
                return true;
            case ApiResponse.STATUS_WALLET_NO_EXSIST:
                getMvpView().onError(R.string.error_wallet_no_exsist);
                return true;
            case ApiResponse.STATUS_IP_EXCEED_LIMIT:
                getMvpView().onError(R.string.error_ip_exceed_limit);
                return true;
            case ApiResponse.STATUS_SERVER_ERROR:
                getMvpView().onError(R.string.error_api_server);
                return true;

        }
        if (StringUtils.isNotEmpty(apiResponse.getMessage())) {
            getMvpView().onError(apiResponse.getMessage());
            return true;
        } else {
            return false;
        }
    }

    public void handleApiError(ANError error) {

        if (error == null || error.getErrorDetail() == null) {
            getMvpView().onError(R.string.error_api_default);
            return;
        }

        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_LOCAL_ERROR
                && error.getErrorDetail().equals(ANConstants.CONNECTION_ERROR)) {
            getMvpView().onError(R.string.error_connection);
            return;
        }

        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_LOCAL_ERROR
                && error.getErrorDetail().equals(ANConstants.REQUEST_CANCELLED_ERROR)) {
            getMvpView().onError(R.string.error_api_retry);
            return;
        }

        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_SERVER_ERROR
                && error.getErrorDetail().equals(ANConstants.RESPONSE_FROM_SERVER_ERROR)) {
            getMvpView().onError(R.string.error_api_server);
            return;
        }

        final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();

        try {
            ApiError apiError = gson.fromJson(error.getErrorBody(), ApiError.class);

            if (apiError == null || apiError.getMessage() == null) {
                getMvpView().onError(R.string.error_api_default);
                return;
            }

            switch (error.getErrorCode()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    getMvpView().onTokenExpire();
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                case HttpsURLConnection.HTTP_NOT_FOUND:
                default:
                    getMvpView().onError(apiError.getMessage());
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            Log.e(TAG, "handleApiError", e);
            getMvpView().onError(R.string.error_api_default);
        }
    }


}
