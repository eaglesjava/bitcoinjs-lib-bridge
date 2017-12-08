package com.bitbill.www.ui.main.receive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SpecificReceiveActivity extends BaseToolbarActivity {

    @BindView(R.id.et_input_amount)
    EditText etInputAmount;
    @BindView(R.id.content_input_specifi_amount)
    RelativeLayout contentInputSpecifiAmount;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;
    @BindView(R.id.content_scan_pay_me)
    LinearLayout contentScanPayMe;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SpecificReceiveActivity.class));
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
        return R.layout.activity_specific_receive;
    }

    @OnClick(value = R.id.btn_ok)
    public void inputConfirmClick(View view) {
        // TODO: 2017/12/8 check输入金额 切换到扫码支付界面
        if (etInputAmount.getText() == null) {
            showMessage("请输入特定金额");
            return;
        }
        contentInputSpecifiAmount.setVisibility(View.GONE);
        contentScanPayMe.setVisibility(View.VISIBLE);
    }
}
