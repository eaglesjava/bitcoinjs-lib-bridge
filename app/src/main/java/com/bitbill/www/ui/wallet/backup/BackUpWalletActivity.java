package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseConfirmDialog;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.widget.PwdDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletActivity extends BaseToolbarActivity {

    private static final String TAG = "BackUpWalletActivity";
    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;

    public static void start(Context context) {
        context.startActivity(new Intent(context, BackUpWalletActivity.class));
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    protected void injectActivity() {

    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        PwdDialogFragment.newInstance(getString(R.string.title_dialog_bakup_wallet), false).setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link BaseConfirmDialog#DIALOG_BTN_POSITIVE}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                    // TODO: 2017/11/22  确定
                    String confirmPwd = ((PwdDialogFragment) dialog).getConfirmPwd();
                    Log.d(TAG, "confirmPwd: " + confirmPwd);
                } else {
                    // TODO: 2017/11/22  取消
                }
            }
        }).show(getSupportFragmentManager(), PwdDialogFragment.TAG);

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_back_up_wallet;
    }

    @OnClick(R.id.btn_wirtten_mnemonic)
    public void wirttenMnemonicClick(View view) {
        //跳转到备份确定界面
        BackupWalletConfirmActivity.start(BackUpWalletActivity.this);
    }
}
