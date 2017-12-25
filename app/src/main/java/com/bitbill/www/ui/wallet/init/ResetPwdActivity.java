package com.bitbill.www.ui.wallet.init;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.widget.EditTextWapper;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPwdActivity extends BaseToolbarActivity<ResetPwdMvpPresenter> implements ResetPwdMvpView {


    @BindView(R.id.etw_trade_pwd_before)
    EditTextWapper etwTradePwdBefore;
    @BindView(R.id.etw_trade_pwd)
    EditTextWapper etwTradePwd;
    @BindView(R.id.etw_trade_pwd_confirm)
    EditTextWapper etwTradePwdConfirm;
    @BindView(R.id.btn_reset_pwd)
    Button btnResetPwd;
    @Inject
    ResetPwdMvpPresenter<WalletModel, ResetPwdMvpView> mResetPwdMvpPresenter;
    private Wallet mWallet;
    private EditTextWapper focusView;
    private boolean cancel;

    public static void start(Context context, Wallet wallet) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
    }

    @Override
    public ResetPwdMvpPresenter getMvpPresenter() {
        return mResetPwdMvpPresenter;
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

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @OnClick(R.id.btn_reset_pwd)
    public void onViewClicked() {
        resetPwd();
    }

    private void resetPwd() {
        cancel = false;
        focusView = null;

        getMvpPresenter().checkOldPwd();

        if (cancel) {
            // There was an error; don't attempt init and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    @Override
    public String getOldPwd() {
        return etwTradePwdBefore.getText();
    }

    @Override
    public String getNewPwd() {
        return etwTradePwd.getText();
    }

    @Override
    public String getConfirmPwd() {
        return etwTradePwdConfirm.getText();
    }

    @Override
    public Wallet getWallet() {
        return mWallet;
    }

    @Override
    public void oldPwdError() {
        etwTradePwdBefore.setError(R.string.error_old_pwd_retry);
        focusView = etwTradePwdBefore;
        cancel = true;
    }

    @Override
    public void resetPwdFail() {
        showMessage("重置密码失败，请重试");
    }

    @Override
    public void resetPwdSuccess() {
        //发送钱包更新事件 其他重新加载数据
        EventBus.getDefault().postSticky(new WalletUpdateEvent());
        showMessage("重置密码成功");
        //关闭相关流程
        finish();

    }

    @Override
    public void requireOldPwd() {

        etwTradePwdBefore.setError(R.string.error_old_pwd_required);
        focusView = etwTradePwdBefore;
        cancel = true;
    }

    @Override
    public void invalidOldPwd() {
        etwTradePwdBefore.setError(R.string.error_invalid_trade_pwd);
        focusView = etwTradePwdBefore;
        cancel = true;

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
}
