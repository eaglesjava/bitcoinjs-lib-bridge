package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class SendAmountActivity extends BaseToolbarActivity {

    @BindView(R.id.et_send_amount)
    EditText etSendAmount;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_btc_cny)
    TextView tvBtcCny;

    private String mAddress;
    private String mAmount;

    public static void start(Context context, String address, String amount) {
        Intent starter = new Intent(context, SendAmountActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, amount);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!hasAmount()) {
            getMenuInflater().inflate(R.menu.send_amount_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_all_amount:
                sendAllAmount();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        etSendAmount.setText(mAmount);
        StringUtils.setEditable(etSendAmount, !hasAmount());
        etSendAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCnyValue();
            }
        });
    }

    private boolean hasAmount() {
        return !StringUtils.isEmpty(mAmount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasAmount()) {
            setTitle(getString(R.string.title_activity_scan_result));
        }
    }

    @Override
    public void initData() {
        updateCnyValue();
    }

    private void updateCnyValue() {
        tvBtcCny.setText(String.format(getString(R.string.text_btc_cny_value), StringUtils.multiplyCnyValue(BitbillApp.get().getBtcValue(), getSendAmount())));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_amount;
    }


    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (!isValidAmount()) {
                    return;
                }
                //跳转到发送确认界面
                SelectWalletActivity.start(SendAmountActivity.this, mAddress, getSendAmount(), false);
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
        //跳转到发送确认界面
        SelectWalletActivity.start(SendAmountActivity.this, mAddress, null, true);
    }

    public String getSendAmount() {
        return etSendAmount.getText().toString();
    }
}
