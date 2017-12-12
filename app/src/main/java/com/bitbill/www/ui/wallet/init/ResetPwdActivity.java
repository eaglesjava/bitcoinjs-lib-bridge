package com.bitbill.www.ui.wallet.init;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.widget.EditTextWapper;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

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
        getMvpPresenter().checkOldPwd();
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
        showMessage("原密码错误，请重新输入");
    }

    @Override
    public void resetPwdFail() {
        showMessage("重置密码失败，请重试");
    }

    @Override
    public void resetPwdSuccess() {
        //关闭相关流程
        finish();

    }
}
