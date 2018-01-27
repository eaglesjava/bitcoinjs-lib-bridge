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
import com.bitbill.www.common.widget.dialog.BaseConfirmDialog;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.init.CreateWalletIdActivity;
import com.bitbill.www.ui.wallet.init.InitWalletActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportWalletActivity extends BaseToolbarActivity<ImportWalletMvpPresenter> implements ImportWalletMvpView {

    private static final String TAG = "ImportWalletActivity";
    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;
    @BindView(R.id.btn_next)
    Button btnNext;
    @Inject
    ImportWalletMvpPresenter<WalletModel, ImportWalletMvpView> importWalletMvpPresenter;
    private String mFromTag;

    public static void start(Context context, String fromTag) {
        Intent intent = new Intent(context, ImportWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_FROM_TAG, fromTag);
        context.startActivity(intent);
    }

    @Override
    public void importWalletSuccess(Wallet wallet) {
        //进入初始化钱包成功界面
        CreateWalletIdActivity.start(ImportWalletActivity.this, wallet, false, mFromTag);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);

        mFromTag = getIntent().getStringExtra(AppConstants.EXTRA_FROM_TAG);
    }

    @Override
    public void importWalletFail() {

        showMessage(R.string.fail_import_wallet);
    }

    @Override
    public String getMnemonic() {
        return etInputMnemonic.getText().toString().trim();
    }

    @Override
    public void getMnemonicFail() {
        showMessage(R.string.fail_input_menemonic);
    }

    @Override
    public void inputMnemonicError() {
        showMessage(R.string.fail_input_menemonic);
    }

    @Override
    public void hasExsistMnemonic(Wallet wallet) {
        //弹出对话框
        MessageConfirmDialog.newInstance("钱包已存在", "是否重置密码", false).setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                    //传递过去
                    InitWalletActivity.start(ImportWalletActivity.this, wallet, false, mFromTag, true);
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
        setTitle(R.string.title_activity_import_wallet);
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
