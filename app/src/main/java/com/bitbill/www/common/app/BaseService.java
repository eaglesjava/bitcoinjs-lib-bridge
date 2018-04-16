package com.bitbill.www.common.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseInjectControl;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.common.utils.NetworkUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.SingleToast;
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
        Log.d(this.toString(), "onCreate() called");
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
        Log.d(this.toString(), "onDestroy() called");
        detachPresenters();
        SingleToast.clear();
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
    public void onError(String message) {
        if (message != null) {
            showMessage(message);
        } else {
            showMessage(getString(R.string.some_error));
        }
        hideLoading();
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            SingleToast.show(message, this);
        } else {
            SingleToast.show(getString(R.string.some_error), this);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }


    @Override
    public void hideKeyboard() {

    }
}
