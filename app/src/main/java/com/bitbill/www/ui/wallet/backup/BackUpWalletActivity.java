package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.ScreenShotObserverActivity;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.common.widget.dialog.PwdDialogFragment;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.MainActivity;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletActivity extends ScreenShotObserverActivity<BackupWalletMvpPresenter> implements BackupWalletMvpView {

    public static final int REQUEST_BACKUP_CODE = 0x11;
    private static final String TAG = "BackUpWalletActivity";
    private static final int RESULT_BACKUP_WALLET = 0x10;
    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;
    @Inject
    BackupWalletPresenter<WalletModel, BackupWalletMvpView> mBackupWaleltPresenter;
    private PwdDialogFragment pwdDialogFragment;
    private Wallet mWallet;
    private boolean isFromSetting;

    public static void start(Context context, Wallet wallet, boolean isFromSetting) {
        Intent intent = new Intent(context, BackUpWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        intent.putExtra(AppConstants.EXTRA_IS_FROM_SETTING, isFromSetting);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        isFromSetting = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_FROM_SETTING, false);
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
        pwdDialogFragment = PwdDialogFragment.newInstance(getString(R.string.title_dialog_bakup_wallet), getWallet(), false);
        pwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed(String confirmPwd) {

                // 确定加载助记词
                getMvpPresenter().loadMnemonic(pwdDialogFragment.getConfirmPwd());
            }

            @Override
            public void onDialogCanceled() {
                // 取消返回主页
                onBackPressed();

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
        showMessage(R.string.fail_load_mnemonic);
    }

    @Override
    public void getWalletFail() {
        showMessage(R.string.fail_get_wallet_info);
    }

    @Override
    public void getConfirmPwdFail() {
        showMessage(R.string.fail_get_confirm_pwd);
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
        BackupWalletConfirmActivity.start(BackUpWalletActivity.this, getMnemonic(), getWallet(), isFromSetting);
    }

    @Override
    public void onBackPressed() {
        finish();
        //返回首页
        if (!isFromSetting) {
            MainActivity.start(BackUpWalletActivity.this);
        }

    }

    @Override
    public void onScreenShot(File shotFile) {
        super.onScreenShot(shotFile);
        //弹出截图危险提示对话框
        MessageConfirmDialog.newInstance(getString(R.string.hint_bak_wallet_ban_screenshots), true).show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
    }
}
