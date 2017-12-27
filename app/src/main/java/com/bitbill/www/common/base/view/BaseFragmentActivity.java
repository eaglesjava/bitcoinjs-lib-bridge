package com.bitbill.www.common.base.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bitbill.www.common.base.presenter.MvpPresenter;

/**
 * Created by isanwenyu@163.com on 2017/12/16.
 */
public abstract class BaseFragmentActivity extends BaseToolbarActivity {

    private static final String TAG = BaseFragmentActivity.class.getSimpleName();
    protected Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFragment == null) {
            mFragment = getFragment();
        }
        if (mFragment != null && getSupportFragmentManager().findFragmentById(CONTENT_VIEW_ID) == null) {
            //如果没有添加过 添加fragment
            getSupportFragmentManager().beginTransaction().add(CONTENT_VIEW_ID, mFragment).commitAllowingStateLoss();
        }
    }

    /**
     * 子类实现获取添加的碎片
     *
     * @return
     */
    protected abstract Fragment getFragment();

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
