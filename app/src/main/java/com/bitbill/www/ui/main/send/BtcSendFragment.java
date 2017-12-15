package com.bitbill.www.ui.main.send;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by isanwenyu@163.com on 2017/12/9.
 */
public class BtcSendFragment extends BaseFragment {

    @BindView(R.id.et_send_address)
    EditText etSendAddress;
    @BindView(R.id.btn_next)
    Button btnNext;

    public static BtcSendFragment newInstance() {

        Bundle args = new Bundle();

        BtcSendFragment fragment = new BtcSendFragment();
        fragment.setArguments(args);
        return fragment;
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
                if (!isValidAddress()) return;
                SendAmountActivity.start(getBaseActivity(), getSendAddress());
                break;
        }
    }

    public String getSendAddress() {
        return etSendAddress.getText().toString();
    }

    public void setSendAddress(String sendAddress) {
        etSendAddress.setText(sendAddress);
    }

    public boolean isValidAddress() {
        if (StringUtils.isEmpty(getSendAddress())) {
            showMessage("请输入或扫描地址");
            return false;
        }
        return true;
    }

    public void sendSuccess() {
        etSendAddress.setText("");
    }
}
