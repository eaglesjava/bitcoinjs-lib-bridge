package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.app.AppManager;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.dialog.PwdDialogFragment;
import com.bitbill.www.model.entity.eventbus.SendSuccessEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SendConfirmActivity extends BaseToolbarActivity {


    @BindView(R.id.et_send_amount)
    TextView tvSendAmount;
    @BindView(R.id.sb_send_fee)
    SeekBar sbSendFee;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.tv_send_contact)
    TextView tvSendContact;
    @BindView(R.id.et_send_mark)
    EditText etSendMark;
    private String mSendAddress;
    private String mSendAmount;
    private PwdDialogFragment mPwdDialogFragment;
    private Wallet mWallet;

    public static void start(Context context, String address, String sendAmount, Wallet wallet) {

        Intent starter = new Intent(context, SendConfirmActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        starter.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
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
        mPwdDialogFragment = PwdDialogFragment.newInstance("交易密码", getWallet(), false);
        mPwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed() {
                // TODO: 2017/12/14 发送交易
                SendSuccessActivity.start(SendConfirmActivity.this, mSendAddress, mSendAmount);
                sendSuccess();

            }

            @Override
            public void onDialogCanceled() {

            }
        });

    }

    private void sendSuccess() {
        //发送发送成功的事件
        EventBus.getDefault().postSticky(new SendSuccessEvent());
        //关闭相关流程
        AppManager.get().finishActivity(SelectWalletActivity.class);
        AppManager.get().finishActivity(SendAmountActivity.class);
        finish();
    }

    private void showPwdDialog() {
        mPwdDialogFragment.show(getSupportFragmentManager(), PwdDialogFragment.TAG);
    }

    private void hidePwdDialog() {
        mPwdDialogFragment.dismissDialog(PwdDialogFragment.TAG);
    }

    @Override
    public void initData() {
        tvSendAmount.setText(mSendAmount + "BTC");
        tvSendAddress.setText(mSendAddress);
        if (mWallet != null) {
            tvSendContact.setText(mWallet.getName());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_confirm;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        showPwdDialog();
    }

    public Wallet getWallet() {
        return mWallet;
    }
}
