package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.dialog.BaseConfirmDialog;
import com.bitbill.www.common.base.view.dialog.PwdDialogFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletActivity extends BaseToolbarActivity<BackupWalletMvpPresenter> implements BackupWalletMvpView {

    private static final String TAG = "BackUpWalletActivity";
    private static final int RESULT_BACKUP_WALLET = 0x10;
    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;
    @Inject
    BackupWalletPresenter<WalletModel, BackupWalletMvpView> mBackupWaleltPresenter;
    private PwdDialogFragment pwdDialogFragment;
    private Wallet mWallet;

    public static void start(Context context, Wallet wallet) {
        Intent intent = new Intent(context, BackUpWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
    }

    @Override
    public BackupWalletMvpPresenter getMvpPresenter() {
        return mBackupWaleltPresenter;
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
        pwdDialogFragment = PwdDialogFragment.newInstance(getString(R.string.title_dialog_bakup_wallet), false);
        pwdDialogFragment.setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link BaseConfirmDialog#DIALOG_BTN_POSITIVE}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pwdDialogFragment.setAutoDismiss(true);
                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                    if (StringUtils.checkUserPwd(pwdDialogFragment.getConfirmPwd(), mWallet)) {

                        // 确定加载助记词
                        getMvpPresenter().loadMnemonic(pwdDialogFragment.getConfirmPwd());
                    } else {
                        pwdDialogFragment.setAutoDismiss(false);
                        showMessage("请输入正确的密码");
                        return;
                    }

                } else {
                    // 取消返回主页
                    finish();
                    MainActivity.start(BackUpWalletActivity.this);
                }
            }
        });
        showPwdDialog();

    }

    private void showPwdDialog() {
        pwdDialogFragment.show(getSupportFragmentManager(), PwdDialogFragment.TAG);
    }

    private void hidePwdDialog() {
        pwdDialogFragment.dismissDialog(PwdDialogFragment.TAG);
    }

    @Override
    public void initData() {
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
    }

    @Override
    public Wallet getWallet() {
        return mWallet;
    }

    @Override
    public void loadMnemonicSuccess(String mnemonic) {
        //显示助记词
        etInputMnemonic.setText(mnemonic);
    }

    @Override
    public void loadMnemonicFail() {
        showMessage(R.string.error_load_mnemonic_fail);
    }

    @Override
    public void getWalletFail() {
        showMessage(R.string.error_get_wallet_info_fail);
    }

    @Override
    public void getConfirmPwdFail() {
        showMessage(R.string.error_get_confirm_pwd_fail);
        showPwdDialog();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_back_up_wallet;
    }

    public String getMnemonic() {
        return etInputMnemonic.getText().toString();
    }

    @OnClick(R.id.btn_wirtten_mnemonic)
    public void wirttenMnemonicClick(View view) {
        //跳转到备份确定界面
        BackupWalletConfirmActivity.start(BackUpWalletActivity.this, getMnemonic(), getWallet());
    }
}
