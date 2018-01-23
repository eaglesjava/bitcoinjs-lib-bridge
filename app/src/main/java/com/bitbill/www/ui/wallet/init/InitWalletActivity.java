package com.bitbill.www.ui.wallet.init;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.common.widget.EditTextWapper;
import com.bitbill.www.common.widget.PwdStatusView;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.asset.AssetFragment;
import com.bitbill.www.ui.main.my.UseRuleActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 导入钱包
 * Created by isanwenyu@163.com on 2017/11/14.
 */
public class InitWalletActivity extends BaseToolbarActivity<InitWalletMvpPresenter> implements InitWalletMvpView, SyncAddressMvpView {

    @BindView(R.id.etw_trade_pwd)
    EditTextWapper etwTradePwd;
    @BindView(R.id.etw_trade_pwd_confirm)
    EditTextWapper etwTradePwdConfirm;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.init_wallet_form)
    LinearLayout initWalletForm;

    @Inject
    InitWalletMvpPresenter<WalletModel, InitWalletMvpView> initWalletMvpPresenter;
    @Inject
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> mSyncAddressMvpPresentder;
    private boolean isCreateWallet = true;
    private EditTextWapper focusView;
    private boolean cancel;
    private Wallet mWallet;
    private boolean isResetPwd;
    private long mIndexNo;
    private String mFromTag;
    private long mChangeIndexNo;

    public static void start(Context context, Wallet wallet, boolean isCreateWallet) {
        Intent intent = new Intent(context, InitWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_IS_CREATE_WALLET, isCreateWallet);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
    }

    public static void start(Context context, Wallet wallet, boolean isCreateWallet, String fromTag, boolean isResetPwd) {

        Intent intent = new Intent(context, InitWalletActivity.class);
        intent.putExtra(AppConstants.EXTRA_IS_CREATE_WALLET, isCreateWallet);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        intent.putExtra(AppConstants.EXTRA_FROM_TAG, fromTag);
        intent.putExtra(AppConstants.EXTRA_IS_RESET_PWD, isResetPwd);
        context.startActivity(intent);
    }

    @Override
    public void injectComponent() {
        //inject activity
        getActivityComponent().inject(this);
        addPresenter(mSyncAddressMvpPresentder);
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
    protected void handleIntent(Intent intent) {

        isCreateWallet = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_CREATE_WALLET, true);
        isResetPwd = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_RESET_PWD, false);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        mFromTag = getIntent().getStringExtra(AppConstants.EXTRA_FROM_TAG);
    }

    @Override
    public void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_init_wallet;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(isResetPwd ? R.string.title_activity_reset_pwd : (isCreateWallet ? R.string.title_activity_create_wallet : R.string.title_activity_import_wallet));
    }

    @OnClick(value = {R.id.btn_start, R.id.tv_server_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                attemptCreateOrImportWallet();
                break;
            case R.id.tv_server_rule:
                ///跳转到服务条款界面
                UseRuleActivity.start(InitWalletActivity.this);
                break;
        }
    }

    private void attemptCreateOrImportWallet() {

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
    public String getWalletId() {
        return mWallet.getName();
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
    public Wallet getWallet() {
        return mWallet;
    }

    public String getConfirmTradePwd() {
        return etwTradePwdConfirm.getText();
    }

    @Override
    public void createWalletSuccess() {
        if (!isCreateWallet) {
            // 优化检查最新地址索引逻辑
            mSyncAddressMvpPresentder.syncLastAddressIndex(mIndexNo, mChangeIndexNo, getWallet());
        }
        //跳转到穿件钱包成功界面
        InitWalletSuccessActivity.start(InitWalletActivity.this, mWallet, isCreateWallet, false);
        if (AssetFragment.TAG.equals(mFromTag)) {
            //从主页进入才通知钱包刷新
            EventBus.getDefault().postSticky(new WalletUpdateEvent());
        }
    }

    @Override
    public void createWalletFail() {
        // TODO: 2017/11/21 弹出创建钱包失败提示
        showMessage("钱包创建失败，请重试");
    }

    @Override
    public InitWalletMvpPresenter getMvpPresenter() {
        return initWalletMvpPresenter;
    }

    @Override
    public boolean isCreateWallet() {
        return isCreateWallet;
    }

    @Override
    public void initWalletInfoFail() {

        showMessage(R.string.fail_init_wallet);
    }

    @Override
    public void invalidWalletId() {

        showMessage(R.string.fail_get_wallet_info);
    }

    @Override
    public boolean isResetPwd() {
        return isResetPwd;
    }

    @Override
    public void getResponseAddressIndex(long indexNo, long changeIndexNo) {
        mIndexNo = indexNo;
        mChangeIndexNo = changeIndexNo;

    }

    @Override
    public void resetPwdSuccess() {

        //跳转到穿件钱包成功界面
        InitWalletSuccessActivity.start(InitWalletActivity.this, mWallet, isCreateWallet, isResetPwd);
    }

    @Override
    public void resetPwdFail() {
        showMessage(R.string.fail_reset_wallet_pwd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}

