package com.bitbill.www.ui.main.receive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SpecificReceiveActivity extends BaseToolbarActivity {

    @BindView(R.id.et_input_amount)
    EditText etInputAmount;
    @BindView(R.id.tv_btc_cny)
    TextView tvBtcCny;
    private String mReceiveAddress;

    public static void start(Context context, String address) {
        Intent intent = new Intent(context, SpecificReceiveActivity.class);
        intent.putExtra(AppConstants.EXTRA_RECEIVE_ADDRESS, address);

        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mReceiveAddress = intent.getStringExtra(AppConstants.EXTRA_RECEIVE_ADDRESS);
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
        tvBtcCny.setText(String.format(getString(R.string.text_btc_cny_value), StringUtils.multiplyCnyValue(BitbillApp.get().getBtcValue(), getReceiveAmount())));
        etInputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                tvBtcCny.setText(String.format(getString(R.string.text_btc_cny_value), StringUtils.multiplyCnyValue(BitbillApp.get().getBtcValue(), getReceiveAmount())));
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_specific_receive;
    }

    @OnClick(value = R.id.btn_next)
    public void inputConfirmClick(View view) {
        //check输入金额 切换到扫码支付界面
        if (StringUtils.isEmpty(getReceiveAmount())) {
            showMessage("请输入特定金额");
            return;
        }
        ScanPayActivity.start(SpecificReceiveActivity.this, getReceiveAddress(), getReceiveAmount());
    }

    private String getReceiveAmount() {
        return etInputAmount.getText().toString();
    }

    public String getReceiveAddress() {
        return mReceiveAddress;
    }
}
