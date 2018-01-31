package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.UpdateMvpPresenter;
import com.bitbill.www.common.presenter.UpdateMvpView;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.SettingView;
import com.bitbill.www.common.widget.dialog.BaseConfirmDialog;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.common.widget.dialog.UpdateAppDialog;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseToolbarActivity<UpdateMvpPresenter> implements UpdateMvpView {


    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.sv_use_rule)
    SettingView mSvUseRule;
    @BindView(R.id.sv_contact_us)
    SettingView mSvVersionCheck;
    @BindView(R.id.sv_check_version)
    SettingView mSvCheckVersion;
    @Inject
    UpdateMvpPresenter<AppModel, UpdateMvpView> mUpdateMvpPresenter;


    public static void start(Context context) {
        Intent starter = new Intent(context, AboutUsActivity.class);
        context.startActivity(starter);
    }

    @Override
    public UpdateMvpPresenter getMvpPresenter() {
        return mUpdateMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {
        setTitle(R.string.title_activity_about_us);

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
        getMvpPresenter().getConfig();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @OnClick({R.id.sv_use_rule, R.id.sv_contact_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sv_use_rule:
                UseRuleActivity.start(AboutUsActivity.this);
                break;
            case R.id.sv_contact_us:
                //发送邮件
                UIHelper.sendEmail(AboutUsActivity.this, AppConstants.HI_BITBILL_EMAIL);
                break;
            case R.id.sv_check_version:
                //检查版本
                showLoading();
                getMvpPresenter().getConfig();
                break;
        }
    }

    @Override
    public void needUpdateApp(boolean needUpdate, boolean needForce, String updateVersion) {
        if (needUpdate) {
            //弹出更新提示框
            MessageConfirmDialog.newInstance(getString(R.string.dialog_title_update_app), getString(R.string.dialog_msg_latest_version) + updateVersion, getString(R.string.dialog_btn_update), needForce)
                    .setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                                mSvCheckVersion.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //弹出下载对话框
                                        UpdateAppDialog.newInstance(getString(R.string.dialog_title_download_app))
                                                .show(getSupportFragmentManager());
                                    }
                                }, 300);
                            }
                        }
                    })
                    .show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
        }
    }

    @Override
    public void getConfigSuccess(String aversion, String aforceVersion) {
        mSvCheckVersion.setRightText(getString(R.string.text_latest_version) + aversion);
        getMvpPresenter().checkUpdate();
        hideLoading();
    }

    @Override
    public void getConfigFail() {
        hideLoading();
    }
}
