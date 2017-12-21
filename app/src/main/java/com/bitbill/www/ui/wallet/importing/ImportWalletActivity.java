package com.bitbill.www.ui.wallet.importing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.dialog.BaseConfirmDialog;
import com.bitbill.www.common.base.view.dialog.MessageConfirmDialog;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.init.CreateWalletIdActivity;
import com.bitbill.www.ui.wallet.init.ResetPwdActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportWalletActivity extends BaseToolbarActivity<ImportWalletMvpPresenter> implements ImportWalletMvpView {

    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;
    @BindView(R.id.btn_next)
    Button btnNext;
    @Inject
    ImportWalletMvpPresenter<WalletModel, ImportWalletMvpView> importWalletMvpPresenter;
    private boolean isFromAsset;

    public static void start(Context context, boolean isFromAsset) {
        Intent intent = new Intent(context, ImportWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_IS_FROM_ASSET, isFromAsset);
        context.startActivity(intent);
    }

    @Override
    public void importWalletSuccess(Wallet wallet) {
        //进入初始化钱包成功界面
        CreateWalletIdActivity.start(ImportWalletActivity.this, wallet, false, isFromAsset);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);

        isFromAsset = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_FROM_ASSET, false);
    }

    @Override
    public void importWalletFail() {

        showMessage(R.string.error_import_wallet_fail);
    }

    @Override
    public String getMnemonic() {
        return etInputMnemonic.getText().toString().trim();
    }

    @Override
    public void getMnemonicFail() {
        showMessage(R.string.error_input_menemonic_fail);
    }

    @Override
    public void inputMnemonicError() {
        showMessage(R.string.error_input_menemonic_fail);
    }

    @Override
    public void hasExsistMnemonic(Wallet wallet) {
        //弹出对话框
        MessageConfirmDialog.newInstance("钱包已存在", "是否重置密码", false).setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                    //传递过去
                    ResetPwdActivity.start(ImportWalletActivity.this, wallet);
                    finish();
                } else {
                    //取消
                    finish();
                }
            }
        }).show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
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
        return R.layout.activity_import_wallet;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        getMvpPresenter().checkMnemonic();
    }

    @Override
    public ImportWalletMvpPresenter getMvpPresenter() {
        return importWalletMvpPresenter;
    }
}
