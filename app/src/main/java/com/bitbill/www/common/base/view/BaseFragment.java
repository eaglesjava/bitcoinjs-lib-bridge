/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.utils.DialogUtils;
import com.bitbill.www.di.component.ActivityComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public abstract class BaseFragment<P extends MvpPresenter> extends Fragment implements MvpView, BaseViewControl, BaseInjectControl<P> {

    protected LayoutInflater mInflater; //视图填充器
    protected View mView;
    protected P mMvpPresenter;
    private BaseActivity<MvpPresenter> mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    /**
     * fragment的初始化方法
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        injectComponent();
        mMvpPresenter = getMvpPresenter();
        if (getMvpPresenter() != null) {
            getMvpPresenter().onAttach(this);
        }
        onBeforeSetContentLayout();
        init(savedInstanceState);
        if (mView == null) {
            this.mInflater = inflater;
            if (getLayoutId() != 0) {
                mView = mInflater.inflate(getLayoutId(), container, false);
            }
        }
        setUnBinder(ButterKnife.bind(this, mView));
        initData();
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        initView();
        return mView;
    }


    @Override
    public void onDestroyView() {
        if (getMvpPresenter() != null) {
            getMvpPresenter().onDetach();
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity<MvpPresenter> activity = (BaseActivity<MvpPresenter>) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = DialogUtils.showLoadingDialog(this.getContext());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void onTokenExpire() {
        if (mActivity != null) {
            mActivity.onTokenExpire();
        }
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    public BaseActivity<MvpPresenter> getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        DialogUtils.close(mProgressDialog);
        super.onDestroy();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
