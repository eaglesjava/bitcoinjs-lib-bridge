package com.bitbill.www.ui.main.receive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class ScanPayActivity extends BaseToolbarActivity<ScanPayMvpPresenter> implements ScanPayMvpView {
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;
    @Inject
    ScanPayMvpPresenter<ScanPayMvpView> mScanPayMvpPresenter;
    private String mReceiveAddress;
    private String mReceiveAmount;

    public static void start(Context context, String receiveAddress, String receiveAmount) {
        Intent starter = new Intent(context, ScanPayActivity.class);
        starter.putExtra(AppConstants.EXTRA_RECEIVE_ADDRESS, receiveAddress);
        starter.putExtra(AppConstants.EXTRA_RECEIVE_AMOUNT, receiveAmount);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mReceiveAddress = getIntent().getStringExtra(AppConstants.EXTRA_RECEIVE_ADDRESS);
        mReceiveAmount = getIntent().getStringExtra(AppConstants.EXTRA_RECEIVE_AMOUNT);
    }

    @Override
    public ScanPayMvpPresenter getMvpPresenter() {
        return mScanPayMvpPresenter;
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
        tvAddress.setText(getReceiveAddress());
        tvReceiveAmount.setText(getReceiveAmount() + " BTC");
    }

    @Override
    public void initData() {
        getMvpPresenter().createReceiveQrcode();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_pay;
    }

    @Override
    public String getReceiveAddress() {
        return mReceiveAddress;
    }

    @Override
    public void createReceiveQrcodeSuccess(Bitmap bitmap) {
        ivQrcode.setImageBitmap(bitmap);

    }

    @Override
    public void createReceiveQrcodeFail() {
        showMessage(R.string.fail_create_pay_qrcode);
    }

    @Override
    public String getReceiveAmount() {
        return mReceiveAmount;
    }
}
