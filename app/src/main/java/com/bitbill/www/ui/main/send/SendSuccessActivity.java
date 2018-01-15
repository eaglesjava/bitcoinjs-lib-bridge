package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseCompleteActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.ui.main.contact.AddContactByAddressActivity;

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
    private Contact mSendContact;

    public static void start(Context context, String address, String sendAmount, Contact sendContact) {
        Intent starter = new Intent(context, SendSuccessActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        starter.putExtra(AppConstants.EXTRA_SEND_CONTACT, sendContact);
        context.startActivity(starter);
    }


    @Override
    protected void handleIntent(Intent intent) {

        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        mSendContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_SEND_CONTACT);
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
        if (mSendContact == null) {
            btnCreateContact.setVisibility(View.VISIBLE);
            btnCreateContact.setOnClickListener(v -> {
                if (StringUtils.isNotEmpty(mSendAddress)) {
                    AddContactByAddressActivity.start(SendSuccessActivity.this, mSendAddress);
                }

            });
        } else {
            btnCreateContact.setVisibility(View.GONE);
        }

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
        finish();
    }
}
