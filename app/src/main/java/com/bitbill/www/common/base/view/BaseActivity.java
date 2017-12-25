/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.view;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.app.AppManager;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.widget.SingleToast;
import com.bitbill.www.common.utils.DialogUtils;
import com.bitbill.www.common.utils.NetworkUtils;
import com.bitbill.www.di.component.ActivityComponent;
import com.bitbill.www.di.component.DaggerActivityComponent;
import com.bitbill.www.di.module.ActivityModule;
import com.bitbill.www.model.eventbus.NetWorkChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Unbinder;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public abstract class BaseActivity<P extends MvpPresenter> extends AppCompatActivity
        implements MvpView, BaseFragment.Callback, BaseInjectControl<P> {

    protected LayoutInflater mInflater;
    private ProgressDialog mProgressDialog;
    private ActivityComponent mActivityComponent;
    private Unbinder mUnBinder;
    private P mMvpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenOrientation();
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((BitbillApp) getApplication()).getComponent())
                .build();
        injectComponent();
        AppManager.get().addActivity(this);
        handleIntent(getIntent());
        mInflater = getLayoutInflater();
        mMvpPresenter = getMvpPresenter();
        if (getMvpPresenter() != null) {
            getMvpPresenter().onAttach(this);
        }

    }

    private void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * The onNeIntent method is invoked when the activity launchMode is singleTop/singleTask and has an intent to reuse the activity instance in the stack
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * handle the intent <br>
     * If the intent is from {@link #onNewIntent}, pay attention to layout initialization
     *
     * @param intent
     */
    protected void handleIntent(Intent intent) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = DialogUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showMessage(message);
        } else {
            showMessage(getString(R.string.some_error));
        }
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
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    @Override
    public void onTokenExpire() {
        finish();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        DialogUtils.close(mProgressDialog);
        AppManager.get().finishActivity(this);
        super.onDestroy();
        if (getMvpPresenter() != null) {
            getMvpPresenter().onDetach();
        }
        SingleToast.clear();
    }

    /**
     * <pre>
     *  This method will be called when a NetWorkChangedEvent is posted.
     *  Refresh the interface after the main thread runs the network changes.
     * </pre>
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onNetWorkChangedEvent(NetWorkChangedEvent event) {
        event = EventBus.getDefault().removeStickyEvent(NetWorkChangedEvent.class);
        if (event != null) {
            showMessage(event.getMsg());
        }
    }

}
