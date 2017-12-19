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
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.dialog.PwdDialogFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.entity.eventbus.SendSuccessEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetTxElementResponse;
import com.bitbill.www.ui.common.BtcAddressMvpPresentder;
import com.bitbill.www.ui.common.BtcAddressMvpView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SendConfirmActivity extends BaseToolbarActivity<SendConfirmMvpPresenter> implements SendConfirmMvpView, BtcAddressMvpView {


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
    @Inject
    SendConfirmMvpPresenter<WalletModel, SendConfirmMvpView> mSendConfirmMvpPresenter;
    @Inject
    BtcAddressMvpPresentder<WalletModel, BtcAddressMvpView> mBtcAddressMvpPresentder;
    private String mSendAddress;
    private String mSendAmount;
    private PwdDialogFragment mPwdDialogFragment;
    private Wallet mWallet;
    private String mLastAddress;
    private boolean isSendAll;
    private String mTradePwd;
    private List<GetTxElementResponse.UtxoBean> mUnspentList;

    public static void start(Context context, String address, String sendAmount, boolean isSendAll, Wallet wallet) {

        Intent starter = new Intent(context, SendConfirmActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        starter.putExtra(AppConstants.EXTRA_WALLET, wallet);
        starter.putExtra(AppConstants.EXTRA_IS_SEND_ALL, isSendAll);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        isSendAll = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_SEND_ALL, false);
    }

    @Override
    public SendConfirmMvpPresenter getMvpPresenter() {
        return mSendConfirmMvpPresenter;
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
        mPwdDialogFragment = PwdDialogFragment.newInstance("交易密码", getWallet(), false);
        mPwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed(String confirmPwd) {
                mTradePwd = confirmPwd;
                // TODO: 2017/12/14 发送交易
                if (isSendAll) {
                    getMvpPresenter().buildTransaction();
                } else {
                    mBtcAddressMvpPresentder.newAddress();
                }
            }

            @Override
            public void onDialogCanceled() {

            }
        });

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
        getMvpPresenter().requestListUnspent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_confirm;
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        showPwdDialog();
    }

    @Override
    public Wallet getWallet() {
        return mWallet;
    }

    @Override
    public void sendTransactionSuccess() {

        SendSuccessActivity.start(SendConfirmActivity.this, mSendAddress, mSendAmount);
        sendSuccess();
    }

    private void sendSuccess() {
        //发送发送成功的事件
        EventBus.getDefault().postSticky(new SendSuccessEvent());
        //关闭相关流程
        AppManager.get().finishActivity(SelectWalletActivity.class);
        AppManager.get().finishActivity(SendAmountActivity.class);
        finish();
    }

    @Override
    public void sendTransactionFail() {
        showMessage("发送交易失败");
    }

    @Override
    public boolean isSendAll() {
        return isSendAll;
    }

    @Override
    public long getFeeByte() {
        return 0;
    }

    @Override
    public long getSendAmount() {
        return isSendAll ? getWallet().getBtcAmount() : StringUtils.btc2Satoshi(mSendAmount);
    }

    @Override
    public long getMaxFeeByte() {
        return 0;
    }

    @Override
    public long getMinFeeByte() {
        return 0;
    }

    @Override
    public String getSendAddress() {
        return mSendAddress;
    }

    @Override
    public String getTradePwd() {
        return mTradePwd;
    }

    @Override
    public void requireTradePwd() {
        //弹出密码确定框
        showPwdDialog();
    }

    @Override
    public void invalidTradePwd() {
        //弹出密码确定框
        showPwdDialog();
    }

    @Override
    public void getWalletFail() {
        showMessage("获取钱包信息错误，请返回重试");
    }

    @Override
    public void getTxElementFail() {
        // TODO: 2017/12/19 错误处理待定
        showMessage("获取钱包信息错误，请返回重试");
    }

    @Override
    public void getTxElementSuccess(List<GetTxElementResponse.UtxoBean> unspentList) {
        //获取utxo成功
        mUnspentList = unspentList;
    }

    @Override
    public String getNewAddress() {
        return mLastAddress;
    }

    @Override
    public List<GetTxElementResponse.UtxoBean> getUnspentList() {
        return mUnspentList;
    }

    @Override
    public void newAddressFail() {
        sendTransactionFail();
    }

    @Override
    public void newAddressSuccess(String lastAddress) {
        mLastAddress = lastAddress;
        getMvpPresenter().buildTransaction();
    }
}
