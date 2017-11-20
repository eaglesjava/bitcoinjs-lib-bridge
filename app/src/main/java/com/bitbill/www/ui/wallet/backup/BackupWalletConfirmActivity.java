package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class BackupWalletConfirmActivity extends BaseToolbarActivity {

    @BindView(R.id.et_input_mnemonic_confirm)
    EditText etInputMnemonicConfirm;
    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;

    public static void start(Context context) {
        context.startActivity(new Intent(context, BackupWalletConfirmActivity.class));
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

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_backup_wallet_confirm;
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (isMnemonicCorrect()) {
            //跳转到备份成功界面
            BackupWalletSuccessActivity.start(BackupWalletConfirmActivity.this);
        } else {
            // TODO: 2017/11/20 弹出不匹配提示
        }
    }

    /**
     * // TODO: 2017/11/20 校验助记词是否匹配
     *
     * @return
     */
    private boolean isMnemonicCorrect() {
        return true;
    }
}
