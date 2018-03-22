package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.presenter.BtcAddressMvpPresentder;
import com.bitbill.www.common.presenter.BtcAddressMvpView;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BtcReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BtcReceiveFragment extends BaseLazyFragment<BtcReceiveMvpPresenter> implements BtcReceiveMvpView, BtcAddressMvpView {


    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;
    @Inject
    BtcReceiveMvpPresenter<WalletModel, BtcReceiveMvpView> mReceiveMvpPresenter;
    @Inject
    BtcAddressMvpPresentder<BtcModel, BtcAddressMvpView> mBtcAddressMvpPresentder;

    private MenuItem mRefreshItem;
    private Wallet mSelectedWallet;

    public BtcReceiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BtcReceiveFragment.
     */
    public static BtcReceiveFragment newInstance() {
        BtcReceiveFragment fragment = new BtcReceiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public BtcReceiveMvpPresenter getMvpPresenter() {
        return mReceiveMvpPresenter;
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
        tvReceiveAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到特定金额接收界面
                SpecificReceiveActivity.start(getBaseActivity(), getCurrentAddress(), mSelectedWallet == null ? null : mSelectedWallet.getId());
            }
        });
        mRefreshItem = getBaseActivity().findViewById(R.id.action_refresh);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_receive;
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {
    }

    @OnClick(value = {R.id.tv_address, R.id.iv_qrcode})
    public void copyClick(View v) {
        //复制地址到剪切板
        UIHelper.copy(getBaseActivity(), tvAddress.getText().toString());
        showMessage(R.string.msg_copy_address_success);
    }

    public void loadAddress(Wallet selectedWallet) {
        mSelectedWallet = selectedWallet;
        if (mReceiveMvpPresenter != null) {
            mBtcAddressMvpPresentder.loadAddress();

        }

    }

    public void refreshAddress(Wallet selectedWallet) {
        mSelectedWallet = selectedWallet;
        if (mBtcAddressMvpPresentder != null) {
            showLoading();
            mBtcAddressMvpPresentder.checkAddressUsed();

        }

    }

    public void setReceiveAddress(String receiveAddress) {
        tvAddress.setText(receiveAddress);
    }

    @Override
    public void createAddressQrcodeSuccess(Bitmap qrcodeBitmap) {
        ivQrcode.setImageBitmap(qrcodeBitmap);
    }

    @Override
    public void createAddressQrcodeFail() {
        onError(R.string.fail_create_address_qrcode);
    }

    @Override
    public void getSelectedWalletFail() {

        onError(R.string.fail_get_wallet_info);
    }

    @Override
    public void loadAddressFail() {
        onError(R.string.fail_load_address);
    }

    @Override
    public void limitAddress(boolean limit) {
        if (limit) {
            reachAddressIndexLimit();
        } else if (mBtcAddressMvpPresentder != null) {
            mBtcAddressMvpPresentder.refreshAddress(1, 0);

        }
    }

    public String getCurrentAddress() {
        return tvAddress.getText().toString();
    }

    @Override
    public Wallet getWallet() {
        return mSelectedWallet;
    }

    @Override
    public void getWalletFail() {
        onError(R.string.fail_get_wallet_info);
    }

    @Override
    public void refreshAddressFail(boolean isInternal) {
        onError(R.string.fail_refresh_address);
    }

    @Override
    public void refreshAddressSuccess(String lastAddress, boolean isInternal) {
        hideLoading();
        showMessage(R.string.msg_refreshed_address);
        getMvpPresenter().createAddressQrcode(lastAddress);
        setReceiveAddress(lastAddress);

    }

    @Override
    public void reachAddressIndexLimit() {
        onError(R.string.fail_reach_address_index_limit);
    }

    @Override
    public void loadAddressSuccess(String lastAddress) {
        getMvpPresenter().createAddressQrcode(lastAddress);
        setReceiveAddress(lastAddress);
    }
}
