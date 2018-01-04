package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.widget.SettingView;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseToolbarActivity {


    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.sv_use_rule)
    SettingView mSvUseRule;
    @BindView(R.id.sv_version_check)
    SettingView mSvVersionCheck;

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutUsActivity.class);
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

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mTvVersion.setText("v" + DeviceUtil.getAppVersion());
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @OnClick({R.id.sv_use_rule, R.id.sv_version_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sv_use_rule:
                UseRuleActivity.start(AboutUsActivity.this);
                break;
            case R.id.sv_version_check:
                break;
        }
    }
}
