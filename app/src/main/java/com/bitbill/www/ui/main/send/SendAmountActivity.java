package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
