package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.app.AppManager;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.BtcAddressMvpPresentder;
import com.bitbill.www.common.presenter.BtcAddressMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.dialog.BaseConfirmDialog;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.common.widget.dialog.PwdDialogFragment;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.eventbus.SendSuccessEvent;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SendConfirmActivity extends BaseToolbarActivity<SendConfirmMvpPresenter> implements SendConfirmMvpView, BtcAddressMvpView {


    @BindView(R.id.et_send_amount)
    TextView tvSendAmount;
    @BindView(R.id.sb_send_fee)
    SeekBar sbSendFee;
    @BindView(R.id.tv_fee_hint)
    TextView tvFeeHint;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.tv_send_contact)
    TextView tvSendContact;
    @BindView(R.id.et_send_mark)
    EditText etSendMark;
    @Inject
    SendConfirmMvpPresenter<TxModel, SendConfirmMvpView> mSendConfirmMvpPresenter;
    @Inject
    BtcAddressMvpPresentder<AddressModel, BtcAddressMvpView> mBtcAddressMvpPresentder;
    private String mSendAddress;
    private String mSendAmount;
    private PwdDialogFragment mPwdDialogFragment;
    private Wallet mWallet;
    private String mLastAddress;
    private boolean isSendAll;
    private String mTradePwd;
    private List<GetTxElementResponse.UtxoBean> mUnspentList;
    private List<GetTxElementResponse.FeesBean> mFees;
    private int mMinTime;
    private int mBestTime;
    private long mFeeByte;
    private long mBestFeeByte;
    private int mFeeTime;
    private Contact mSendContact;

    public static void start(Context context, String address, String sendAmount, boolean isSendAll, Wallet wallet, Contact sendContact) {

        Intent starter = new Intent(context, SendConfirmActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        starter.putExtra(AppConstants.EXTRA_WALLET, wallet);
        starter.putExtra(AppConstants.EXTRA_IS_SEND_ALL, isSendAll);
        starter.putExtra(AppConstants.EXTRA_SEND_CONTACT, sendContact);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        if (mWallet != null) {
            mWallet.__setDaoSession(getApp().getDaoSession());
        }
        isSendAll = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_SEND_ALL, false);
        mSendContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_SEND_CONTACT);
    }

    @Override
    public SendConfirmMvpPresenter getMvpPresenter() {
        return mSendConfirmMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
        addPresenter(mBtcAddressMvpPresentder);

    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mPwdDialogFragment = PwdDialogFragment.newInstance(getString(R.string.dialog_title_wallet_pwd), getWallet(), false);
        mPwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed(String confirmPwd) {
                mTradePwd = confirmPwd;
                // TODO: 2017/12/14 发送交易
                if (isSendAll) {
                    getMvpPresenter().buildTransaction();
                } else {
                    mBtcAddressMvpPresentder.refreshAddress(1, 1);
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

        setTvSendAmount(mSendAmount);
        tvSendAddress.setText(mSendAddress);
        if (mWallet != null) {
            tvSendContact.setText(mWallet.getName());
        }
        getMvpPresenter().requestListUnspent();
    }

    private void setTvSendAmount(String amount) {
        tvSendAmount.setText(amount);
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
    public void sendTransactionSuccess(String txHash) {

        SendSuccessActivity.start(SendConfirmActivity.this, mSendAddress, mSendAmount, mSendContact, txHash);
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
    public void sendTransactionFail(String message) {
        if (StringUtils.isEmpty(message)) {
            showMessage(R.string.fail_send_transaction);
        } else {
            showMessage(message);
        }
    }

    @Override
    public boolean isSendAll() {
        return isSendAll;
    }

    @Override
    public long getFeeByte() {
        return mFeeByte;
    }

    @Override
    public long getSendAmount() {
        return StringUtils.btc2Satoshi(tvSendAmount.getText().toString());
    }

    public int getMaxFeeByte() {
        if (StringUtils.isEmpty(mFees)) return 0;
        return mFees.get(mFees.size() - 1).getFee();
    }

    public int getMinFeeByte() {
        if (StringUtils.isEmpty(mFees)) return 0;
        return mFees.get(0).getFee();
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
    public void getTxElementSuccess(List<GetTxElementResponse.UtxoBean> unspentList, List<GetTxElementResponse.FeesBean> fees) {
        mUnspentList = unspentList;
        mFees = fees;

        if (!StringUtils.isEmpty(mFees)) {
            //按时间正序排列
            Collections.sort(mFees, (o1, o2) -> o1.getFee() - o2.getFee());
            mFeeByte = getBestFeeByte();
            mFeeTime = getBestTime();
            refreshSeekBar();
            //估算手续费
            getMvpPresenter().computeFee();
        }

    }

    private void refreshSeekBar() {
        sbSendFee.setMax(100);
        int feeRange = getMaxFeeByte() - getMinFeeByte();
        if (feeRange != 0) {
            sbSendFee.setProgress((int) (getBestFeeByte() * 1.0 / feeRange * 100));
        }
        sbSendFee.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int seekFee = (int) ((progress * (getMaxFeeByte() - getMinFeeByte()) * 1.0 / 100) + getMinFeeByte());
                updateFeeLayout(seekFee);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateFeeLayout(int progress) {
        int index = -1;
        for (int i = 0; i < mFees.size() - 1; i++) {
            int currentFee = mFees.get(i).getFee();
            int nextFee = mFees.get(i + 1).getFee();
            if (progress == currentFee) {
                index = i;
                break;
            }
            if (progress == nextFee) {
                index = i + 1;
                break;
            }
            if (progress > currentFee && progress < nextFee) {
                if (progress < (currentFee + nextFee) / 2) {
                    index = i;
                } else {
                    index = i + 1;
                }
                break;
            } else {
                continue;
            }
        }
        if (index == -1) {
            //取最好的手续费
            mFeeByte = getBestFeeByte();
            mFeeTime = getBestTime();
        } else {
            mFeeByte = mFees.get(index).getFee();
            mFeeTime = mFees.get(index).getTime();
        }

        //估算手续费
        getMvpPresenter().computeFee();
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
    public void compteFee(long fee) {
        if (StringUtils.isEmpty(mFees)) {
            return;
        }
        refreshFeeHintLayout(StringUtils.satoshi2btc(fee));
        if (isSendAll && mWallet.getBalance() - fee > 0) {
            setTvSendAmount(StringUtils.satoshi2btc(mWallet.getBalance() - fee));
        }
    }

    private void refreshFeeHintLayout(String feeBtc) {
        StringBuilder hintbuilder = new StringBuilder();

        hintbuilder.append("平均出块时间")
                .append(StringUtils.formatTime(mFeeTime))
                .append("，需耗费")
                .append(feeBtc)
                .append("BTC");
        tvFeeHint.setText(hintbuilder.toString());
    }

    @Override
    public void amountNoEnough() {
        MessageConfirmDialog.newInstance("余额不足，请返回重新选择", true)
                .setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
    }

    @Override
    public String getRemark() {
        return etSendMark.getText().toString();
    }

    @Override
    public void refreshAddressFail(boolean isInternal) {
        sendTransactionFail(getString(R.string.fail_refresh_change_address));
    }

    @Override
    public void refreshAddressSuccess(String lastAddress, boolean isInternal) {
        if (isInternal) {
            mLastAddress = lastAddress;
            getMvpPresenter().buildTransaction();
        } else {
            refreshAddressFail(isInternal);
        }
    }

    @Override
    public void reachAddressIndexLimit() {
        showMessage(R.string.fail_reach_address_index_limit);
    }

    @Override
    public void loadAddressSuccess(String lastAddress) {

    }

    @Override
    public void loadAddressFail() {

    }


    public int getBestTime() {
        for (GetTxElementResponse.FeesBean fee : mFees) {
            if (fee.isBest()) {
                return fee.getTime();
            }
        }
        return 0;
    }

    public int getBestFeeByte() {
        if (StringUtils.isEmpty(mFees)) return 0;
        for (GetTxElementResponse.FeesBean fee : mFees) {
            if (fee.isBest()) {
                return fee.getFee();
            }
        }
        return 0;
    }
}
