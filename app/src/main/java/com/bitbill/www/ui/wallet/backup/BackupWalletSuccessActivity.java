package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.R;
import com.bitbill.www.common.app.AppManager;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseCompleteActivity;
import com.bitbill.www.ui.main.MainActivity;

import butterknife.OnClick;

public class BackupWalletSuccessActivity extends BaseCompleteActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, BackupWalletSuccessActivity.class));
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

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
        return R.layout.activity_backup_wallet_success;
    }

    @OnClick(R.id.btn_complete)
    public void onViewClicked() {
        completeAction();
    }

    /**
     * 完成动作
     */
    @Override
    protected void completeAction() {
        //跳转到主页
        MainActivity.start(BackupWalletSuccessActivity.this);
        //关闭备份流程
        AppManager.get().finishActivity(BackUpWalletActivity.class);
        AppManager.get().finishActivity(BackupWalletConfirmActivity.class);
        finish();

    }
}
