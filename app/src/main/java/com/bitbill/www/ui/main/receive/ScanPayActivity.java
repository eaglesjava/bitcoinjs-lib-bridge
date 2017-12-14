package com.bitbill.www.ui.main.receive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import butterknife.BindView;

public class ScanPayActivity extends BaseToolbarActivity {
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;

    public static void start(Context context) {
        Intent starter = new Intent(context, ScanPayActivity.class);
        context.startActivity(starter);
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
        return R.layout.activity_scan_pay;
    }
}
