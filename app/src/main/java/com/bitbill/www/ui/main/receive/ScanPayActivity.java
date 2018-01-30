package com.bitbill.www.ui.main.receive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.eventbus.ReceiveAmountEvent;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.network.socket.ContextBean;
import com.bitbill.www.model.wallet.network.socket.UnConfirmed;
import com.bitbill.www.ui.wallet.info.transfer.TransferDetailsActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ScanPayActivity extends BaseToolbarActivity<ScanPayMvpPresenter> implements ScanPayMvpView, ParseTxInfoMvpView {
    public static final String TAG = "ScanPayActivity";
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;
    @Inject
    ScanPayMvpPresenter<TxModel, ScanPayMvpView> mScanPayMvpPresenter;
    @Inject
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> mViewParseTxInfoMvpPresenter;
    private String mReceiveAddress;
    private String mReceiveAmount;
    private String mTxHash;
    private Long mWalletId;

    public static void start(Context context, String receiveAddress, String receiveAmount, Long walletId) {
        Intent starter = new Intent(context, ScanPayActivity.class);
        starter.putExtra(AppConstants.EXTRA_RECEIVE_ADDRESS, receiveAddress);
        starter.putExtra(AppConstants.EXTRA_RECEIVE_AMOUNT, receiveAmount);
        starter.putExtra(AppConstants.EXTRA_WALLET_ID, walletId);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mReceiveAddress = getIntent().getStringExtra(AppConstants.EXTRA_RECEIVE_ADDRESS);
        mReceiveAmount = getIntent().getStringExtra(AppConstants.EXTRA_RECEIVE_AMOUNT);
        mWalletId = getIntent().getLongExtra(AppConstants.EXTRA_WALLET_ID, -1);
    }

    @Override
    public ScanPayMvpPresenter getMvpPresenter() {
        return mScanPayMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
        addPresenter(mViewParseTxInfoMvpPresenter);
    }

    @Override
    public void onBeforeSetContentLayout() {
        setTitle(R.string.title_activity_scan_pay);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        tvAddress.setText(getReceiveAddress());
        tvReceiveAmount.setText(getReceiveAmount() + " BTC");
    }

    @Override
    public void initData() {
        getMvpPresenter().createReceiveQrcode();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_pay;
    }

    @Override
    public String getReceiveAddress() {
        return mReceiveAddress;
    }

    @Override
    public void createReceiveQrcodeSuccess(Bitmap bitmap) {
        ivQrcode.setImageBitmap(bitmap);

    }

    @Override
    public void createReceiveQrcodeFail() {
        showMessage(R.string.fail_create_pay_qrcode);
    }

    @Override
    public String getReceiveAmount() {
        return mReceiveAmount;
    }

    @Override
    public String getTxHash() {
        return mTxHash;
    }

    @Override
    public void getTxHashFail() {

    }

    @Override
    public void addressMatchTx(boolean match, TxElement txElement) {
        if (match && txElement != null) {
            //地址匹配交易解析交易
            List<TxElement> txElements = new ArrayList<>();
            txElements.add(txElement);
            mViewParseTxInfoMvpPresenter.parseTxInfo(txElements);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveAmountEvent(ReceiveAmountEvent receiveAmountEvent) {
        //重新加载联系人信息
        if (receiveAmountEvent != null) {
            UnConfirmed unConfirmed = (UnConfirmed) receiveAmountEvent.getData();
            ContextBean context = unConfirmed.getContext();
            if (context != null) {
                try {
                    double unConfirmedAmount = Double.parseDouble(context.getAmount());
                    double receiveAmount = Double.parseDouble(getReceiveAmount());
                    if (unConfirmedAmount > 0 && receiveAmount > 0 && unConfirmedAmount >= receiveAmount) {
                        //金额匹配
                        mTxHash = context.getTxHash();
                        getMvpPresenter().getTxInfo();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void requireTxInfoList() {

    }

    @Override
    public void getTxInfoListFail() {

    }

    @Override
    public void parsedTxItemList(List<TxRecord> txRecords) {
        if (StringUtils.isEmpty(txRecords)) {
            return;
        }
        //筛选相关的交易
        for (TxRecord txRecord : txRecords) {
            if (mWalletId != null && mWalletId > 0l && mWalletId.equals(txRecord.getWalletId())) {
                //找到交易记录并打开交易详情
                TransferDetailsActivity.start(ScanPayActivity.this, txRecord, TAG);
                finish();
                break;
            }
        }
    }

    @Override
    public void parsedTxItemListFail() {

    }
}
