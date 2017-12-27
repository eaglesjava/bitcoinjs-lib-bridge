package com.bitbill.www.ui.wallet.init;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.EditTextWapper;
import com.bitbill.www.common.widget.dialog.SupportCoinDialog;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletIdActivity extends BaseToolbarActivity<CreateWalletIdMvpPresenter> implements CreateWalletIdMvpView {

    @BindView(R.id.etw_wallet_name)
    EditTextWapper etwWalletName;
    @Inject
    CreateWalletIdMvpPresenter<WalletModel, CreateWalletIdMvpView> mCreateWalletIdMvpPresenter;
    private Wallet mWallet;
    private boolean isCreateWallet;
    private boolean isFromAsset;

    public static void start(Context context, Wallet wallet, boolean isCreateWallet, boolean isFromAsset) {

        Intent starter = new Intent(context, CreateWalletIdActivity.class);
        starter.putExtra(AppConstants.EXTRA_WALLET, wallet);
        starter.putExtra(AppConstants.EXTRA_IS_CREATE_WALLET, isCreateWallet);
        starter.putExtra(AppConstants.EXTRA_IS_FROM_ASSET, isFromAsset);
        context.startActivity(starter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(isCreateWallet ? R.string.title_activity_create_wallet : R.string.title_activity_import_wallet);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = ((Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET));
        isCreateWallet = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_CREATE_WALLET, true);
        isFromAsset = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_FROM_ASSET, false);
    }

    @Override
    public CreateWalletIdMvpPresenter getMvpPresenter() {
        return mCreateWalletIdMvpPresenter;
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
        SupportCoinDialog.newInstance("目前支持以下币种", true, "我知道了")
                .show(getSupportFragmentManager(), SupportCoinDialog.TAG);

    }

    @Override
    public void initData() {
        if (mWallet != null && StringUtils.isWalletIdValid(mWallet.getName())) {
            etwWalletName.setText(mWallet.getName());
            //设置钱包id输入框不可用
            etwWalletName.setEditable(false);
            etwWalletName.setTextColor(getResources().getColor(R.color.white_60));
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_wallet_id;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        next();
    }

    @Override
    public void requireWalletId() {
        etwWalletName.setError(R.string.error_wallet_id_required);

    }

    @Override
    public void invalidWalletId() {
        etwWalletName.setError(R.string.error_invalid_wallet_id);

    }

    @Override
    public void requireWalletIdLength() {

        etwWalletName.setError(R.string.error_wallet_id_length_required);

    }

    @Override
    public String getWalletId() {
        return etwWalletName.getText();
    }

    @Override
    public void checkWalletIdSuccess() {
        goInitWalletActivity();
    }

    @Override
    public void hasWalletIdExsist() {
        if (isCreateWallet) {
            showMessage(getString(R.string.error_wallet_id_exsist));
        } else {
            goInitWalletActivity();
        }
    }

    private void goInitWalletActivity() {
        if (mWallet == null)
            mWallet = new Wallet();
        mWallet.setName(getWalletId());
        //跳转到创建钱包界面
        InitWalletActivity.start(CreateWalletIdActivity.this, mWallet, isCreateWallet, isFromAsset);
    }

    @Override
    public void checkWalletIdFail() {
        showMessage(getString(R.string.error_wallet_id_check_fail));

    }

    @Override
    public void isValidIdStart() {
        etwWalletName.setError(R.string.error_invalid_wallet_id_start);
    }

    private void next() {
        // Reset errors.
        etwWalletName.removeError();

        getMvpPresenter().checkWalletId();


    }

}
