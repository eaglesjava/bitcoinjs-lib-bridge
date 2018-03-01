package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.network.entity.GetConfigResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2018/1/31.
 */
@PerActivity
public class UpdatePresenter<M extends AppModel, V extends UpdateMvpView> extends ModelPresenter<M, V> implements UpdateMvpPresenter<M, V> {
    @Inject
    public UpdatePresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }


    @Override
    public void getConfig() {
        getCompositeDisposable().add(getModelManager()
                .getConfig()
                .compose(applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<GetConfigResponse>>() {
                    @Override
                    public void onNext(ApiResponse<GetConfigResponse> getConfigResponseApiResponse) {
                        super.onNext(getConfigResponseApiResponse);
                        if (handleApiResponse(getConfigResponseApiResponse)) {
                            return;
                        }
                        if (getConfigResponseApiResponse.isSuccess()) {
                            GetConfigResponse data = getConfigResponseApiResponse.getData();
                            if (data != null) {
                                String aforceVersion = data.getAforceVersion();
                                getModelManager().setForceVersion(aforceVersion);
                                String aversion = data.getAversion();
                                getModelManager().setUpdateVersion(aversion);
                                getModelManager().setUpdateLog(data.getUpdateLog());
                                getMvpView().getConfigSuccess(aversion, aforceVersion);
                                getModelManager().setApkUrl(data.getApkUrl());
                            }
                        } else {
                            getMvpView().getConfigFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().getConfigFail();
                        }
                    }
                }));
    }

    @Override
    public void checkUpdate() {
        String updateVersion = getModelManager().getUpdateVersion();
        String apkUrl = getModelManager().getApkUrl();
        if (!StringUtils.isUrl(apkUrl) || StringUtils.isEmpty(updateVersion)) {
            return;
        }
        boolean needUpdate = StringUtils.needUpdate(DeviceUtil.getAppVersion(), updateVersion);
        String forceVersion = getModelManager().getForceVersion();
        boolean needForce = StringUtils.needUpdate(DeviceUtil.getAppVersion(), forceVersion);
        if (needUpdate || needForce) {
            String updateLog = getModelManager().getUpdateLog();
            getMvpView().needUpdateApp(needUpdate, needForce, updateVersion, apkUrl, updateLog);
        }
    }
}
