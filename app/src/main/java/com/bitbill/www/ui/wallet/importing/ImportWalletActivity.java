package com.bitbill.www.ui.wallet.importing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.model.wallet.WalletModel;
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

    public static void start(Context context) {
        context.startActivity(new Intent(context, ImportWalletActivity.class));
    }

    @Override
    public void importWalletSuccess() {

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

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        //进入主界面
        MainActivity.start(ImportWalletActivity.this);
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return importWalletMvpPresenter;
    }
}
