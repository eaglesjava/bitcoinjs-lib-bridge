package com.bitbill.www.common.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseInjectControl;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.component.DaggerServiceComponent;
import com.bitbill.www.di.component.ServiceComponent;
import com.bitbill.www.di.module.ServiceModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isanwenyu on 2018/3/28.
 */

public abstract class BaseService<P extends MvpPresenter> extends Service implements MvpView, BaseInjectControl<P> {

    private BitbillApp mBitbillApp;
    private ServiceComponent mServiceComponent;
    private P mMvpPresenter;
    private List<MvpPresenter> mPresenters = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBitbillApp = BitbillApp.get();
        mServiceComponent = DaggerServiceComponent.builder().serviceModule(new ServiceModule(this)).applicationComponent(mBitbillApp.getComponent())
                .build();
        injectComponent();
        addPresenter(mMvpPresenter = getMvpPresenter());
        attachPresenters();

    }

    public BitbillApp getApp() {
        return mBitbillApp;
    }

    public ServiceComponent getServiceComponent() {
        return mServiceComponent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detachPresenters();
    }

    @Override
    public void attachPresenters() {
        if (!StringUtils.isEmpty(getPresenters())) {
            for (MvpPresenter mvpPresenter : getPresenters()) {
                if (mMvpPresenter != null) {
                    mvpPresenter.onAttach(this);

                }
            }
        }
    }

    @Override
    public void detachPresenters() {
        if (!StringUtils.isEmpty(getPresenters())) {
            for (MvpPresenter mvpPresenter : getPresenters()) {
                if (mvpPresenter != null) {
                    mvpPresenter.onDetach();

                }
            }
        }
    }

    @Override
    public List<MvpPresenter> getPresenters() {
        return mPresenters;
    }

    @Override
    public void addPresenter(MvpPresenter mvpPresenter) {
        mPresenters.add(mvpPresenter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showKeyboard() {

    }

    @Override
    public void onTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void hideKeyboard() {

    }
}
