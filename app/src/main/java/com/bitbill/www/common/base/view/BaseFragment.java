/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.component.ActivityComponent;
import com.bitbill.www.model.eventbus.NetWorkChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public abstract class BaseFragment<P extends MvpPresenter> extends Fragment implements MvpView, BaseViewControl, BaseInjectControl<P> {

    protected LayoutInflater mInflater; //视图填充器
    protected View mView;
    protected P mMvpPresenter;
    private List<MvpPresenter> mPresenters = new ArrayList<>();
    private BaseActivity<MvpPresenter> mActivity;
    private Unbinder mUnBinder;
    private BitbillApp mApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mApp = BitbillApp.get();
    }

    @Override
    public BitbillApp getApp() {
        return mApp;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        addPresenter(mMvpPresenter = getMvpPresenter());
        attachPresenters();
        onBeforeSetContentLayout();
        init(savedInstanceState);
        if (mView == null) {
            this.mInflater = inflater;
            if (getLayoutId() != 0) {
                mView = mInflater.inflate(getLayoutId(), container, false);
            }
        }
        setUnBinder(ButterKnife.bind(this, mView));
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        initView();
        initData();
        return mView;
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
    public void addPresenter(MvpPresenter mvpPresenter) {
        mPresenters.add(mvpPresenter);
    }

    @Override
    public List<MvpPresenter> getPresenters() {
        return mPresenters;
    }

    @Override
    public void onDestroyView() {
        detachPresenters();
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
        if (!isAdded()) {
            return;
        }
        if (mActivity != null) {
            mActivity.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mActivity != null) {
            mActivity.hideLoading();
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
        hideLoading();
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
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
    public void showKeyboard() {
        if (mActivity != null) {
            mActivity.showKeyboard();
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
        super.onDestroy();
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

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
