package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.utils.StringUtils;
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
public class BtcReceiveFragment extends BaseLazyFragment<BtcReceiveMvpPresenter> implements BtcReceiveMvpView {


    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;
    @Inject
    BtcReceiveMvpPresenter<WalletModel, BtcReceiveMvpView> mReceiveMvpPresenter;
    private String mSendAddress;

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
                SpecificReceiveActivity.start(getBaseActivity());
            }
        });
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
        StringUtils.copy(tvAddress.getText().toString(), getBaseActivity());
        showMessage(R.string.R_string_toast_copy_address_success);
    }

    public void refreshAddress(Wallet selectedWallet) {
        mReceiveMvpPresenter.reloadAddress(selectedWallet);

    }

    public void setSendAddress(String sendAddress) {
        mSendAddress = sendAddress;
        tvAddress.setText(sendAddress);
    }

    @Override
    public void createAddressQrcodeSuccess(Bitmap qrcodeBitmap) {
        ivQrcode.setImageBitmap(qrcodeBitmap);
    }

    @Override
    public void createAddressQrcodeFail() {
        showMessage(R.string.error_create_address_qrcode);

    }

    @Override
    public void refreshAddressFail() {

        showMessage(R.string.error_refresh_address);
    }
}
