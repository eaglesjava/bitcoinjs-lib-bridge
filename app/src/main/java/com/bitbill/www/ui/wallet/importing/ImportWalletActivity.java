package com.bitbill.www.ui.wallet.importing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.MainActivity;

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
    private Wallet mWallet;//传递过来的钱包对象

    public static void start(Context context, Wallet wallet) {
        Intent intent = new Intent(context, ImportWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
    }

    @Override
    public void importWalletSuccess() {
        //进入主界面
        MainActivity.start(ImportWalletActivity.this);
    }

    @Override
    public void importWalletFail() {

    }

    @Override
    public void injectActivity() {
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
        mWallet = ((Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        getMvpPresenter().importWallet(mWallet);
    }

    @Override
    public ImportWalletMvpPresenter getMvpPresenter() {
        return importWalletMvpPresenter;
    }
}
