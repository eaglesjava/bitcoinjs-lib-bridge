package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SendAmountActivity extends BaseToolbarActivity {

    @BindView(R.id.et_send_amount)
    EditText etSendAmount;
    @BindView(R.id.tv_send_all_amount)
    TextView tvSendAllAmount;
    @BindView(R.id.btn_next)
    Button btnNext;
    private String mAddress;

    public static void start(Context context, String address) {
        Intent starter = new Intent(context, SendAmountActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
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
        StringUtils.setTextViewUnLine(tvSendAllAmount);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_amount;
    }


    @OnClick({R.id.tv_send_all_amount, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_all_amount:
                sendAllAmount();
                break;
            case R.id.btn_next:
                if (!isValidAmount()) {
                    return;
                }
                //跳转到发送确认界面
                SelectWalletActivity.start(SendAmountActivity.this, mAddress, getSendAmount());
                break;
        }
    }

    private boolean isValidAmount() {
        if (StringUtils.isEmpty(getSendAmount())) {
            showMessage("请输入发送金额");
            return false;
        }
        return true;
    }

    /**
     * 发送全部余额
     */
    private void sendAllAmount() {

    }

    public String getSendAmount() {
        return etSendAmount.getText().toString();
    }
}
