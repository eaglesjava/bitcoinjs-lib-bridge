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
import com.bitbill.www.model.contact.db.entity.Contact;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SendAmountActivity extends BaseToolbarActivity<GetExchangeRateMvpPresenter> implements GetExchangeRateMvpView {

    @BindView(R.id.et_send_amount)
    AmountEditText etSendAmount;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcValue;
    @Inject
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> mGetExchangeRateMvpPresenter;

    private String mAddress;
    private Contact mSendContact;

    public static void start(Context context, String address, Contact sendContact) {
        Intent starter = new Intent(context, SendAmountActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_CONTACT, sendContact);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_SEND_CONTACT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_amount_menu, menu);
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
    public GetExchangeRateMvpPresenter getMvpPresenter() {
        return mGetExchangeRateMvpPresenter;
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
        etSendAmount.addTextChangedListener(new TextWatcher() {
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
        tvBtcValue.setText(BitbillApp.get().getBtcValue(getSendAmount()));
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
                SelectWalletActivity.start(SendAmountActivity.this, mAddress, getSendAmount(), false, mSendContact);
                break;
        }
    }

    private boolean isValidAmount() {
        if (StringUtils.isEmpty(getSendAmount())) {
            showMessage(R.string.msg_input_send_amount);
            return false;
        }
        if (StringUtils.isZero(getSendAmount())) {
            showMessage(R.string.msg_input_gt_zero_amount);
            return false;
        }
        return true;
    }

    /**
     * 发送全部余额
     */
    private void sendAllAmount() {
        //跳转到发送确认界面
        SelectWalletActivity.start(SendAmountActivity.this, mAddress, null, true, mSendContact);
    }

    public String getSendAmount() {
        return etSendAmount.getText().toString();
    }

    @Override
    public void getBtcRateSuccess(double cnyRate, double usdRate) {

    }
}
