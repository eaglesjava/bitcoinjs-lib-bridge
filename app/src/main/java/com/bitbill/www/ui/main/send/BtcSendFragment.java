package com.bitbill.www.ui.main.send;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.model.wallet.WalletModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by isanwenyu@163.com on 2017/12/9.
 */
public class BtcSendFragment extends BaseFragment<BtcSendMvpPresenter> implements BtcSendMvpView {

    @BindView(R.id.et_send_address)
    EditText etSendAddress;
    @BindView(R.id.btn_next)
    Button btnNext;
    @Inject
    BtcSendMvpPresenter<WalletModel, BtcSendMvpView> mBtcSendMvpPresenter;

    public static BtcSendFragment newInstance() {

        Bundle args = new Bundle();

        BtcSendFragment fragment = new BtcSendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BtcSendMvpPresenter getMvpPresenter() {
        return mBtcSendMvpPresenter;
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

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_send;
    }


    @OnClick({R.id.iv_contact, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_contact:
                // TODO: 2017/12/14 跳转到联系人界面
                showMessage("跳转到联系人界面");
                break;
            case R.id.btn_next:
                //跳转到发送金额界面
                getMvpPresenter().validateBtcAddress();
                break;
        }
    }

    @Override
    public String getSendAddress() {
        return etSendAddress.getText().toString();
    }

    public void setSendAddress(String sendAddress) {
        etSendAddress.setText(sendAddress);
    }


    public void sendSuccess() {
        etSendAddress.setText("");
    }

    @Override
    public void validateAddress(boolean validate) {
        if (validate) {
            SendAmountActivity.start(getBaseActivity(), getSendAddress(), null);
        } else {
            showMessage("请输入合法的地址");
        }
    }

    @Override
    public void requireAddress() {
        showMessage("请输入或扫描地址");

    }
}
