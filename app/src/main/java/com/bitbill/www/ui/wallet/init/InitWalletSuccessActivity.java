package com.bitbill.www.ui.wallet.init;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.app.AppManager;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseCompleteActivity;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.guide.GuideActivity;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;
import com.bitbill.www.ui.wallet.importing.ImportWalletActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class InitWalletSuccessActivity extends BaseCompleteActivity {

    @BindView(R.id.btn_bak_wallet)
    Button btnBakWallet;
    @BindView(R.id.btn_bak_wallet_delay)
    Button btnBakWalletDelay;
    @BindView(R.id.tv_hint_title)
    TextView tvHintTitle;
    @BindView(R.id.tv_hint_content)
    TextView tvHintContent;
    @BindView(R.id.ll_bottom_btns)
    View llBottonBtns;
    private Wallet mWallet;
    private boolean isCreateWallet = true;//默认创建钱包成功界面

    public static void start(Context context, Wallet wallet, boolean isCreateWallet) {
        Intent intent = new Intent(context, InitWalletSuccessActivity.class);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        intent.putExtra(AppConstants.EXTRA_IS_CREATE_WALLET, isCreateWallet);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        isCreateWallet = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_CREATE_WALLET, true);
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

        tvHintContent.setText(isCreateWallet ? R.string.hint_create_wallet_success : R.string.hint_import_wallet_success);
        tvHintTitle.setText(isCreateWallet ? R.string.title_wallet_create_success : R.string.title_wallet_import_success);
        llBottonBtns.setVisibility(isCreateWallet ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(isCreateWallet ? R.string.title_activity_create_wallet : R.string.title_activity_import_wallet);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_init_wallet_success;
    }

    @OnClick({R.id.btn_bak_wallet, R.id.btn_bak_wallet_delay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bak_wallet:
                //跳转到备份钱包界面
                BackUpWalletActivity.start(InitWalletSuccessActivity.this, mWallet);
                break;
            case R.id.btn_bak_wallet_delay:
                //跳转到主页
                MainActivity.start(InitWalletSuccessActivity.this);
                break;
        }
        finishAbout();
    }

    /**
     * 完成动作
     */
    @Override
    protected void completeAction() {
        //跳转到主页
        MainActivity.start(InitWalletSuccessActivity.this);
        finishAbout();

    }

    private void finishAbout() {
        //关闭初始化钱包流程
        AppManager.get().finishActivity(GuideActivity.class);
        AppManager.get().finishActivity(CreateWalletIdActivity.class);
        AppManager.get().finishActivity(InitWalletActivity.class);
        if (!isCreateWallet) {
            AppManager.get().finishActivity(ImportWalletActivity.class);
        }
        finish();
    }
}
