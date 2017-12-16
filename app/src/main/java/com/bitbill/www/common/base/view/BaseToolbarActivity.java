package com.bitbill.www.common.base.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;

import butterknife.ButterKnife;

/**
 * 带title的基础布局处理
 * Created by isanwenyu@163.com on 2016/2/24.
 */
public abstract class BaseToolbarActivity<P extends MvpPresenter> extends BaseActivity<P> implements BaseViewControl {
    public static final int CONTENT_VIEW_ID = R.id.content_view;
    private static final int BASE_VIEW_ID = R.layout.activity_base_toolbar;
    private static final ViewGroup.LayoutParams LAYOUT_PARAMS = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    protected LinearLayout mParentView;
    protected ViewGroup mContentView;
    protected android.support.v7.widget.Toolbar mToolbar;
//    protected EmptyLayout mErrorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentLayout();
        setContentView(getLayoutId());
        // 通过注解绑定控件
        setUnBinder(ButterKnife.bind(this));
        init(savedInstanceState);
        initView();
        initData();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(BASE_VIEW_ID);
        initBaseView();
        if (layoutResID != 0) {
            mContentView.addView(getLayoutInflater().inflate(layoutResID, null), 0, LAYOUT_PARAMS);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(getLayoutInflater().inflate(BASE_VIEW_ID, null));
        initBaseView();
        if (view != null) {
            mContentView.addView(view, 0, LAYOUT_PARAMS);
        }
    }

    /**
     * 初始化baseview
     */
    private void initBaseView() {
        mContentView = (ViewGroup) findViewById(CONTENT_VIEW_ID);

        //初始化Toolbar
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (hasTitleBar()) {
            initTitleBar();
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化titleBar
     */
    public void initTitleBar() {
        if (hasHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasHomeAsUpEnabled());
            //返回按钮监听事件
            mToolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
        }
    }

    /**
     * 是否有返回
     *
     * @return
     */
    protected boolean hasHomeAsUpEnabled() {
        return true;
    }

    /**
     * 控制titleBar是否显示
     *
     * @return
     */
    protected boolean hasTitleBar() {
        return true;
    }

}