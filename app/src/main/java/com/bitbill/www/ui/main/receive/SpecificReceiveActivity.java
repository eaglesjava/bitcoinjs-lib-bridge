package com.bitbill.www.ui.main.receive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.GetExchangeRateMvpPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.AmountEditText;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SpecificReceiveActivity extends BaseToolbarActivity<GetExchangeRateMvpPresenter> implements GetExchangeRateMvpView {

    @BindView(R.id.et_input_amount)
    AmountEditText etInputAmount;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcCny;
    @Inject
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> mGetExchangeRateMvpPresenter;
    private String mReceiveAddress;
    private Long mWalletId;

    public static void start(Context context, String address, Long walletId) {
        Intent intent = new Intent(context, SpecificReceiveActivity.class);
        intent.putExtra(AppConstants.EXTRA_RECEIVE_ADDRESS, address);
        intent.putExtra(AppConstants.EXTRA_WALLET_ID, walletId);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mReceiveAddress = intent.getStringExtra(AppConstants.EXTRA_RECEIVE_ADDRESS);
        mWalletId = getIntent().getLongExtra(AppConstants.EXTRA_WALLET_ID, -1l);
    }

    @Override
    public GetExchangeRateMvpPresenter getMvpPresenter() {
        return mGetExchangeRateMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {
        setTitle(R.string.title_activity_specific_receive);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        etInputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                updateBtcValue();
            }
        });

    }

    private void updateBtcValue() {
        tvBtcCny.setText(BitbillApp.get().getBtcValue(getReceiveAmount()));
    }

    @Override
    public void initData() {
        if (getApp().hasBtcRate()) {
            updateBtcValue();
        } else {
            getMvpPresenter().getExchangeRate();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_specific_receive;
    }

    @OnClick(value = R.id.btn_next)
    public void inputConfirmClick(View view) {
        //check输入金额 切换到扫码支付界面
        if (!isValidAmount()) {
            return;
        }
        ScanPayActivity.start(SpecificReceiveActivity.this, getReceiveAddress(), getReceiveAmount(), mWalletId);
    }

    private boolean isValidAmount() {
        if (StringUtils.isEmpty(getReceiveAmount())) {
            showMessage(R.string.msg_input_specifc_amount);
            return false;
        }
        if (StringUtils.isZero(getReceiveAmount())) {
            showMessage(R.string.msg_input_gt_zero_amount);
            return false;
        }
        return true;
    }

    private String getReceiveAmount() {
        return etInputAmount.getText().toString();
    }

    public String getReceiveAddress() {
        return mReceiveAddress;
    }

    @Override
    public void getBtcRateSuccess(double cnyRate, double usdRate) {
        updateBtcValue();
    }
}
