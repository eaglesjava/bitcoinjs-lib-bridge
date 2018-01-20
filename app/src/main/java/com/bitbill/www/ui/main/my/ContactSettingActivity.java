package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.dialog.InputDialogFragment;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.common.widget.dialog.PwdDialogFragment;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.OnClick;

public class ContactSettingActivity extends BaseToolbarActivity<ContactSettingMvpPresenter> implements ContactSettingMvpView {

    @Inject
    ContactSettingMvpPresenter<ContactModel, ContactSettingMvpView> mContactSettingMvpPresenter;
    private PwdDialogFragment mBackupContactPwdDialogFragment;
    private InputDialogFragment mRecoverContactInputDialogFragment;
    private Handler mDialogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String walletKey = getMvpPresenter().getWalletKey();
            //显示联系人key对话框
            MessageConfirmDialog.newInstance(getString(R.string.setting_backup_contact), walletKey, getString(R.string.positive_copy_contact_key), true)
                    .setConfirmDialogClickListener((dialog, which) -> {
                        UIHelper.copy(ContactSettingActivity.this, walletKey);
                        showMessage(R.string.msg_contact_key_copied);
                    })
                    .show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
        }
    };


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

        setTitle(R.string.title_activity_contact_setting);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        Wallet defaultWallet = BitbillApp.get().getDefaultWallet();
        mBackupContactPwdDialogFragment = PwdDialogFragment.newInstance(getString(R.string.setting_backup_contact), defaultWallet, false, String.format(getString(R.string.msg_input_defualt_wallet_pwd), defaultWallet.getName()));
        mBackupContactPwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed(String confirmPwd) {

                mDialogHandler.sendMessageDelayed(new Message(), 100);
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
    public void recoverContactSuccess(int size) {
        showMessage(String.format(getString(R.string.msg_recover_contact_success), size));
        EventBus.getDefault().postSticky(new ContactUpdateEvent());
    }

    @Override
    public void receoverContactFail() {
        showMessage(R.string.msg_recover_fail);

    }

    @Override
    public void receoverContactsNull() {
        showMessage(R.string.msg_no_contact_recover);
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
