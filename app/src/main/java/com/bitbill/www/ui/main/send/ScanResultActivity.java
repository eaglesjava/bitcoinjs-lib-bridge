package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.AmountEditText;
import com.bitbill.www.model.eventbus.RefreshExchangeRateEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanResultActivity extends BaseToolbarActivity implements GetExchangeRateMvpView {

    @BindView(R.id.et_send_amount)
    AmountEditText etSendAmount;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcValue;
    @BindView(R.id.ll_receiver_address)
    View mAddressLayout;
    @BindView(R.id.tv_send_address)
    TextView mSendAddressTextView;

    private String mAddress;
    private String mAmount;

    public static void start(Context context, String address, String amount) {
        Intent starter = new Intent(context, ScanResultActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, amount);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        try {
            mAmount = StringUtils.satoshi2btc(StringUtils.btc2Satoshi(mAmount));
        } catch (Exception e) {
            e.printStackTrace();
            mAmount = "0.00";
        }
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {
        setTitle(R.string.title_activity_send_amount);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        etSendAmount.setText(mAmount);
        StringUtils.setEditable(etSendAmount, false);
        if (hasAmount() && StringUtils.isNotEmpty(mAddress)) {
            mAddressLayout.setVisibility(View.VISIBLE);
            mSendAddressTextView.setText(mAddress);
        } else {
            mAddressLayout.setVisibility(View.GONE);
        }
    }

    private boolean hasAmount() {
        return !StringUtils.isEmpty(mAmount);
    }

    @Override
    public void initData() {
        if (getApp().hasBtcRate()) {
            updateBtcValue();
        }

    }

    private void updateBtcValue() {
        tvBtcValue.setText(BitbillApp.get().getBtcValue(getSendAmount()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_result;
    }


    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //跳转到发送确认界面
                SelectWalletActivity.start(ScanResultActivity.this, mAddress, getSendAmount(), false, null);
                break;
        }
    }

    public String getSendAmount() {
        return etSendAmount.getText().toString();
    }

    @Override
    public void getBtcRateSuccess(double cnyRate, double usdRate) {
        updateBtcValue();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshExchangeRateEvent(RefreshExchangeRateEvent event) {
        if (event != null) {
            updateBtcValue();
        }
    }
}
