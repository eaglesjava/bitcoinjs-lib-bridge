package com.bitbill.www.ui.wallet.init;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.widget.EditTextWapper;
import com.bitbill.www.common.base.view.widget.PwdStatusView;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
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
    private boolean isCreateWallet = true;
    private EditTextWapper focusView;
    private boolean cancel;
    private Wallet mWallet;

    public static void start(Context context, boolean isCreateWallet) {
        Intent intent = new Intent(context, InitWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_IS_CREATE_WALLET, isCreateWallet);
        context.startActivity(intent);
    }


    @Override
    public void injectActivity() {
        //inject activity
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
        isCreateWallet = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_CREATE_WALLET, true);
        btnStart.setText(isCreateWallet ? R.string.btn_start_create : R.string.btn_start_import);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_init_wallet;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(isCreateWallet ? R.string.title_activity_create_wallet : R.string.title_activity_import_wallet);
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

        cancel = false;
        focusView = null;

        // init wallet logic
        initWalletMvpPresenter.initWallet();

        if (cancel) {
            // There was an error; don't attempt init and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    @Override
    public String getWalletName() {
        return etwWalletName.getText();
    }

    public String getTradePwd() {
        return etwTradePwd.getText();
    }

    @Override
    public void requireTradeConfirmPwd() {
        etwTradePwdConfirm.setError(R.string.error_confirm_trade_pwd_required);
        focusView = etwTradePwdConfirm;
        cancel = true;
    }

    @Override
    public void isPwdInConsistent() {
        etwTradePwdConfirm.setError(R.string.error_trade_pwd_inconsistent);
        focusView = etwTradePwdConfirm;
        cancel = true;

    }

    @Override
    public void requireTradePwd() {
        etwTradePwd.setError(R.string.error_trade_pwd_required);
        focusView = etwTradePwd;
        cancel = true;
    }

    @Override
    public void invalidTradePwd() {
        etwTradePwd.setError(R.string.error_invalid_trade_pwd);
        focusView = etwTradePwd;
        cancel = true;

    }

    @Override
    public void requireWalletName() {
        etwWalletName.setError(R.string.error_wallet_name_required);
        focusView = etwWalletName;
        cancel = true;

    }

    @Override
    public void invalidWalletName() {
        etwWalletName.setError(R.string.error_invalid_wallet_name);
        focusView = etwWalletName;
        cancel = true;

    }

    public String getConfirmTradePwd() {
        return etwTradePwdConfirm.getText();
    }

    @Override
    public void initWalletSuccess(Wallet wallet) {
        this.mWallet = wallet;
        if (!isCreateWallet) {
            //跳转到导入钱包流程
            ImportWalletActivity.start(this, mWallet);
        } else {
            getMvpPresenter().createMnemonic(mWallet);
        }
    }

    @Override
    public void initWalletFail() {
        // TODO: 2017/11/21 弹出创建钱包失败提示
        showMessage("钱包创建失败，请重试");
    }

    @Override
    public void createMnemonicSuccess(String encryptMnemonicHash) {
        if (isCreateWallet) {
            //跳转到穿件钱包成功界面
            InitWalletSuccessActivity.start(InitWalletActivity.this, mWallet, isCreateWallet);
        }
    }

    @Override
    public void createMnemonicFail() {
        // TODO: 2017/11/21 弹出创建钱包失败提示
        showMessage("钱包创建失败，请重试");

    }

    @Override
    public InitWalletMvpPresenter getMvpPresenter() {
        return initWalletMvpPresenter;
    }
}

