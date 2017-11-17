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
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.ui.wallet.importing.ImportWalletActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 导入钱包
 * Created by isanwenyu@163.com on 2017/11/14.
 */
public class InitWalletActivity extends BaseToolbarActivity<InitWalletMvpPresenter> implements InitWalletMvpView {

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
    @BindView(R.id.init_wallet_form)
    LinearLayout initWalletForm;
    @BindView(R.id.wallet_form)
    ScrollView walletForm;

    @Inject
    InitWalletMvpPresenter<WalletModel, InitWalletMvpView> initWalletMvpPresenter;
    private int mCreateOrImportStatus;

    public static void start(Context context, int createOrImport) {
        Intent intent = new Intent(context, InitWalletActivity.class);
        intent.putExtra(CREATE_IMPORT_ETRA, createOrImport);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initWalletMvpPresenter.onDetach();
    }

    @Override
    public void onBeforeSetContentLayout() {
        //inject activity
        getActivityComponent().inject(this);
        initWalletMvpPresenter.onAttach(this);


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
        mCreateOrImportStatus = getIntent().getIntExtra(CREATE_IMPORT_ETRA, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_init_wallet;
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
            initWalletMvpPresenter.initWallet();
            if (isCreateWallet()) {
                initWalletMvpPresenter.createMnemonic();
            }

        }
    }

    private boolean isCreateWallet() {
        return mCreateOrImportStatus == CREATE_WALLET;
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

    @Override
    public void initWalletSuccess() {
//        if (!isCreateWallet()) {
        //跳转到导入钱包流程
        ImportWalletActivity.start(this);
//        }
    }

    @Override
    public void initWalletFail() {

    }

    @Override
    public void createMnemonicSuccess() {

    }

    @Override
    public void createMnemonicFail() {

    }
}

