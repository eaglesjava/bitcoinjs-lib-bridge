package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseCompleteActivity;

import butterknife.BindView;

public class SendSuccessActivity extends BaseCompleteActivity {

    @BindView(R.id.tv_hint_title)
    TextView tvHintTitle;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.et_send_amount)
    TextView tvSendAmount;
    @BindView(R.id.tv_hint_content)
    TextView tvHintContent;
    @BindView(R.id.btn_create_contact)
    Button btnCreateContact;
    private String mSendAddress;
    private String mSendAmount;

    public static void start(Context context, String address, String sendAmount) {
        Intent starter = new Intent(context, SendSuccessActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        context.startActivity(starter);
    }


    @Override
    protected void handleIntent(Intent intent) {

        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
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
        tvSendAddress.setText(mSendAddress);
        tvSendAmount.setText(mSendAmount + " BTC");

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_success;
    }

    /**
     * 完成动作
     */
    @Override
    protected void completeAction() {
        // TODO: 2017/12/16 关闭相关流程
        finish();
    }
}
