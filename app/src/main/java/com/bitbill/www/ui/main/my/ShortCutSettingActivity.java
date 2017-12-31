package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import butterknife.BindView;

public class ShortCutSettingActivity extends BaseToolbarActivity<ShortCutSettingMvpPresenter> {
    @Inject
    ShortCutSettingMvpPresenter<AppModel, ShortCutSettingMvpView> mShortCutSettingMvpPresenter;
    @BindView(R.id.switch_shortcut)
    Switch switchShortcut;

    public static void start(Context context) {
        Intent starter = new Intent(context, ShortCutSettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    public ShortCutSettingMvpPresenter getMvpPresenter() {
        return mShortCutSettingMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        switchShortcut.setChecked(getMvpPresenter().isShortcutShown());
        switchShortcut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getMvpPresenter().setShortcutShown(isChecked);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shortcut_setting;
    }

}
