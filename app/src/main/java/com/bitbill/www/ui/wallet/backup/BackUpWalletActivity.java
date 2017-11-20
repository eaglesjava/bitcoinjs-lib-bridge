package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletActivity extends BaseToolbarActivity {

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
