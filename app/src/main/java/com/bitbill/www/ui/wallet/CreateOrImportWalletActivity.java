package com.bitbill.www.ui.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.widget.EditTextWapper;
import com.bitbill.www.common.base.view.widget.PwdStatusView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 导入钱包
 * Created by isanwenyu@163.com on 2017/11/14.
 */
public class CreateOrImportWalletActivity extends BaseToolbarActivity {

    public static final int CREATE_WALLET = 0;
    public static final int IMPORT_WALLET = 1;
    public static final String CREATE_IMPORT_ETRA = "create_import_etra";
    @BindView(R.id.etw_wallet_name)
    EditTextWapper etwWalletName;
    @BindView(R.id.etw_trade_pwd)
    EditTextWapper etwTradePwd;
    @BindView(R.id.etw_trade_pwd_confirm)
    EditTextWapper etwTradePwdConfirm;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.create_import_wallet_form)
    LinearLayout createImportWalletForm;
    @BindView(R.id.wallet_form)
    ScrollView walletForm;

    public static void start(Context context, int createOrImport) {
        Intent intent = new Intent(context, CreateOrImportWalletActivity.class);
        intent.putExtra(CREATE_IMPORT_ETRA, createOrImport);
        context.startActivity(intent);
    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        etwTradePwd.addInputTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    return;
                }
                // TODO: 2017/11/17 强度算法计算密码强度
                if (s.length() > 10) {
                    etwTradePwd.setStrongLevel(PwdStatusView.StrongLevel.STRONG);
                } else if (s.length() > 6) {
                    etwTradePwd.setStrongLevel(PwdStatusView.StrongLevel.NORMAL);
                } else if (s.length() > 2) {
                    etwTradePwd.setStrongLevel(PwdStatusView.StrongLevel.WEAK);
                } else {
                    etwTradePwd.setStrongLevel(PwdStatusView.StrongLevel.DANGER);
                }
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_import_wallet;
    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        attemptCreateOrImportWallet();
    }

    private void attemptCreateOrImportWallet() {
        // Reset errors.
        etwWalletName.removeError();
        etwTradePwd.removeError();
        etwTradePwdConfirm.removeError();

        boolean cancel = false;
        EditTextWapper focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(getConfirmTradePwd()) || !isPasswordValid(getConfirmTradePwd()) || !isPwdConsistent()) {
            etwTradePwdConfirm.setError(R.string.error_trade_pwd_inconsistent);
            focusView = etwTradePwdConfirm;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(getTradePwd()) || !isPasswordValid(getTradePwd())) {
            etwTradePwd.setError(R.string.error_invalid_trade_pwd);
            focusView = etwTradePwd;
            cancel = true;
        }

        // Check for a valid wallet name.
        if (TextUtils.isEmpty(getWalletName()) || !isWalletNameValid(getWalletName())) {
            etwWalletName.setError(R.string.error_wallet_name_required);
            focusView = etwWalletName;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt create or import and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the wallet create or import attempt.
            showProgress(true);
            // TODO: 2017/11/14 create or import wallet logic
        }
    }

    private void showProgress(boolean show) {

    }

    private boolean isWalletNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6 && password.length() < 20;
    }

    private boolean isPwdConsistent() {
        return TextUtils.equals(getTradePwd(), getConfirmTradePwd());
    }

    public String getWalletName() {
        return etwWalletName.getText();
    }

    public String getTradePwd() {
        return etwTradePwd.getText();
    }

    public String getConfirmTradePwd() {
        return etwTradePwdConfirm.getText();
    }
}

