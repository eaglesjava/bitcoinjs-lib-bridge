package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.dialog.InputDialogFragment;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.common.widget.dialog.PwdDialogFragment;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.OnClick;

public class ContactSettingActivity extends BaseToolbarActivity<ContactSettingMvpPresenter> implements ContactSettingMvpView {

    @Inject
    ContactSettingMvpPresenter<ContactModel, ContactSettingMvpView> mContactSettingMvpPresenter;
    private PwdDialogFragment mBackupContactPwdDialogFragment;
    private InputDialogFragment mRecoverContactInputDialogFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, ContactSettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ContactSettingMvpPresenter getMvpPresenter() {
        return mContactSettingMvpPresenter;
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

        mBackupContactPwdDialogFragment = PwdDialogFragment.newInstance(getString(R.string.setting_backup_contact), BitbillApp.get().getWallets().get(0), false);
        mBackupContactPwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed(String confirmPwd) {
                // TODO: 2017/12/29 对话框未显示
                String walletKey = getMvpPresenter().getWalletKey();
                //显示联系人key对话框
                MessageConfirmDialog.newInstance(null, walletKey, "复制联系人密钥", false)
                        .setConfirmDialogClickListener((dialog, which) -> StringUtils.copy(walletKey, ContactSettingActivity.this))
                        .show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
            }

            @Override
            public void onDialogCanceled() {

            }
        });
        mRecoverContactInputDialogFragment = InputDialogFragment.newInstance(getString(R.string.setting_recover_contact), getString(R.string.hint_contact_key), false);
        mRecoverContactInputDialogFragment.setOnConfirmInputListener(new InputDialogFragment.OnConfirmInputListener() {
            @Override
            public void onPwdCnfirmed(String confirmInput) {
                getMvpPresenter().recoverContact(confirmInput);
            }

            @Override
            public void onDialogCanceled() {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_setting;
    }


    @Override
    public void requireContactKey() {
        //弹出输入密钥对话框
        showContactKeyInputDialog();
    }

    private void showContactKeyInputDialog() {
        mRecoverContactInputDialogFragment.show(getSupportFragmentManager(), InputDialogFragment.TAG);
    }

    @Override
    public void recoverContactSuccess() {
        showMessage("恢复联系人成功");
        EventBus.getDefault().postSticky(new ContactUpdateEvent());
    }

    @Override
    public void receoverContactFail() {
        showMessage("恢复联系人失败");

    }

    @OnClick({R.id.tv_backup_contact, R.id.tv_recover_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_backup_contact:
                mBackupContactPwdDialogFragment.show(getSupportFragmentManager(), PwdDialogFragment.TAG);
                break;
            case R.id.tv_recover_contact:
                showContactKeyInputDialog();
                break;
        }
    }
}
