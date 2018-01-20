package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import butterknife.BindView;

public class UseRuleActivity extends BaseToolbarActivity {
    @BindView(R.id.tv_use_rule)
    TextView mTvUseRule;

    public static void start(Context context) {
        Intent starter = new Intent(context, UseRuleActivity.class);
        context.startActivity(starter);
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void onBeforeSetContentLayout() {
        setTitle(R.string.title_activity_use_rule);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mTvUseRule.setText(Html.fromHtml(getString(R.string.html_use_rule)));

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_use_rule;
    }
}
