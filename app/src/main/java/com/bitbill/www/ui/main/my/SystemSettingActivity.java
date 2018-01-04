package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.widget.SettingView;
import com.bitbill.www.common.widget.dialog.ListSelectDialog;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;

import javax.inject.Inject;

import butterknife.BindView;

public class SystemSettingActivity extends BaseToolbarActivity<SystemSettingMvpPresenter> implements SystemSettingMvpView {
    @Inject
    SystemSettingMvpPresenter<AppModel, SystemSettingMvpView> mSystemSettingMvpPresenter;
    @BindView(R.id.sv_sound)
    SettingView mSvSound;
    @BindView(R.id.sv_currency)
    SettingView mSvCurrency;

    private ListSelectDialog mListSelectDialog;

    public static void start(Context context) {
        Intent starter = new Intent(context, SystemSettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    public SystemSettingMvpPresenter getMvpPresenter() {
        return mSystemSettingMvpPresenter;
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
        mSvSound.setOnRightSwitchCheckedChangeListener((buttonView, isChecked) -> getMvpPresenter().setSoundEnabled(isChecked));
        String[] currencyArray = getResources().getStringArray(R.array.currency_type);
        mListSelectDialog = ListSelectDialog.newInstance(currencyArray);
        mListSelectDialog.setOnListSelectItemClickListener(position -> {
            mSvCurrency.setRightText(currencyArray[position]);
            AppPreferences.SelectedCurrency selectedCurrency = position == 0 ? AppPreferences.SelectedCurrency.CNY : AppPreferences.SelectedCurrency.USD;
            getMvpPresenter().setSelectedCurrency(selectedCurrency);
            BitbillApp.get().setSelectedCurrency(selectedCurrency);
        });
        mSvCurrency.setOnClickListener(v -> mListSelectDialog.show(getSupportFragmentManager(), ListSelectDialog.TAG));

    }

    @Override
    public void initData() {
        mSvSound.setSwitchChecked(getMvpPresenter().isSoundEnable());
        mSvCurrency.setRightText(getMvpPresenter().getSelectedCurrency().name());

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_setting;
    }

}
