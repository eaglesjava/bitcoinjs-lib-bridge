package com.bitbill.www.ui.wallet.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletActivity extends BaseToolbarActivity {

    @BindView(R.id.btn_bak_wallet)
    Button btnBakWallet;
    @BindView(R.id.btn_bak_wallet_delay)
    Button btnBakWalletDelay;

    public static void start(Context context) {
        context.startActivity(new Intent(context, CreateWalletActivity.class));
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
        return R.layout.activity_create_wallet;
    }

    @OnClick({R.id.btn_bak_wallet, R.id.btn_bak_wallet_delay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bak_wallet:
                break;
            case R.id.btn_bak_wallet_delay:
                //跳转到主页
                MainActivity.start(CreateWalletActivity.this);
                break;
        }
    }
}
