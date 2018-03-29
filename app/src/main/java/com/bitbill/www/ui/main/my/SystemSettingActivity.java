package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.SoundUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.SettingView;
import com.bitbill.www.common.widget.dialog.ListSelectDialog;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.prefs.AppPreferences;
import com.bitbill.www.ui.main.MainActivity;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;

public class SystemSettingActivity extends BaseToolbarActivity<SystemSettingMvpPresenter> implements SystemSettingMvpView {
    @Inject
    SystemSettingMvpPresenter<AppModel, SystemSettingMvpView> mSystemSettingMvpPresenter;
    @BindView(R.id.sv_sound)
    SettingView mSvSound;
    @BindView(R.id.sv_currency)
    SettingView mSvCurrency;
    @BindView(R.id.sv_languge)
    SettingView mSvLanguge;
    @BindArray(R.array.language_type)
    String[] mLanguageArray;


    private ListSelectDialog mCurrencySelectDialog;
    private ListSelectDialog mLangugeSelectDialog;
    private String[] mCurrencyArray;

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
        setTitle(R.string.title_activity_system_setting);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mSvSound.setSwitchChecked(getMvpPresenter().isSoundEnable());
        mSvSound.setOnRightSwitchCheckedChangeListener((buttonView, isChecked) -> {
            getMvpPresenter().setSoundEnabled(isChecked);
            //开启播放声音
            if (isChecked) {
                SoundUtils.playSound(R.raw.diaoluo_da);
            }
        });
        mCurrencyArray = getResources().getStringArray(R.array.currency_type);
        mCurrencySelectDialog = ListSelectDialog.newInstance(mCurrencyArray);
        mCurrencySelectDialog.setOnListSelectItemClickListener(position -> {
            mSvCurrency.setRightText(mCurrencyArray[position]);
            AppPreferences.SelectedCurrency selectedCurrency = position == 0 ? AppPreferences.SelectedCurrency.CNY : AppPreferences.SelectedCurrency.USD;
            getMvpPresenter().setSelectedCurrency(selectedCurrency);
        });
        mSvCurrency.setOnClickListener(v -> mCurrencySelectDialog.show(getSupportFragmentManager(), ListSelectDialog.TAG));

        mLangugeSelectDialog = ListSelectDialog.newInstance(mLanguageArray);
        mLangugeSelectDialog.setOnListSelectItemClickListener(position -> {
            mSvLanguge.setRightText(mLanguageArray[position]);
            Locale locale = null;
            switch (position) {
                case 0:
                    locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case 1:
                    locale = Locale.TRADITIONAL_CHINESE;
                    break;
                case 2:
                    locale = Locale.ENGLISH;
                    break;
            }
            if (getMvpPresenter().needUpdateLocale(locale)) {
                getMvpPresenter().setSelectedLocale(locale);
                restartAct();
            }

        });
        mSvLanguge.setOnClickListener(v -> mLangugeSelectDialog.show(getSupportFragmentManager(), ListSelectDialog.TAG));

    }

    @Override
    public void initData() {
        mSvCurrency.setRightText(AppPreferences.SelectedCurrency.CNY.equals(getMvpPresenter().getSelectedCurrency()) ? mCurrencyArray[0] : mCurrencyArray[1]);
        String language = null;
        Locale selectedLocale = getMvpPresenter().getSelectedLocale();
        if (StringUtils.isZhCN(selectedLocale)) {
            language = mLanguageArray[0];
        } else if (StringUtils.equals(Locale.TRADITIONAL_CHINESE.getCountry(), selectedLocale.getCountry())) {
            language = mLanguageArray[1];
        } else {
            language = mLanguageArray[2];
        }

        mSvLanguge.setRightText(language);

    }

    /**
     * 重启当前Activity
     */
    private void restartAct() {
        finish();
        Intent _Intent = new Intent(this, SystemSettingActivity.class);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
