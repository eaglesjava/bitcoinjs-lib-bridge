package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseCompleteActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.ui.main.contact.AddContactByAddressActivity;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.ll_add_contact)
    LinearLayout llAddContact;
    @BindView(R.id.tv_tx_hash)
    TextView mTvTxHash;
    @BindView(R.id.tx_view_in_blockchain)
    TextView mTxViewInBlockchain;
    @BindView(R.id.ll_view_txhash)
    LinearLayout mLlViewTxhash;
    private String mSendAddress;
    private String mSendAmount;
    private Contact mSendContact;
    private String mTxHash;

    public static void start(Context context, String address, String sendAmount, Contact sendContact, String txHash) {
        Intent starter = new Intent(context, SendSuccessActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        starter.putExtra(AppConstants.EXTRA_SEND_CONTACT, sendContact);
        starter.putExtra(AppConstants.EXTRA_SEND_TXHASH, txHash);
        context.startActivity(starter);
    }


    @Override
    protected void handleIntent(Intent intent) {

        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        mSendContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_SEND_CONTACT);
        mTxHash = getIntent().getStringExtra(AppConstants.EXTRA_SEND_TXHASH);
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
        setTitle(R.string.title_activity_send_success);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        tvSendAddress.setText(mSendAddress);
        tvSendAmount.setText(mSendAmount + " ic_coin_btc");
        if (mSendContact == null) {
            llAddContact.setVisibility(View.VISIBLE);
            btnCreateContact.setOnClickListener(v -> {
                if (StringUtils.isNotEmpty(mSendAddress)) {
                    AddContactByAddressActivity.start(SendSuccessActivity.this, mSendAddress);
                }

            });
        } else {
            llAddContact.setVisibility(View.GONE);
        }
        if (StringUtils.isNotEmpty(mTxHash)) {
            mLlViewTxhash.setVisibility(View.VISIBLE);
            mTvTxHash.setText(mTxHash);
        } else {
            mLlViewTxhash.setVisibility(View.GONE);
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


    @OnClick({R.id.tv_tx_hash, R.id.tx_view_in_blockchain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tx_hash:
                UIHelper.copy(SendSuccessActivity.this, mTxHash);
                showMessage(R.string.copy_tx_hash);
                break;
            case R.id.tx_view_in_blockchain:
                UIHelper.openBrower(SendSuccessActivity.this, AppConstants.TX_BLOCK_CHAIN_PREFIX + mTxHash);
                break;
        }
    }
}
