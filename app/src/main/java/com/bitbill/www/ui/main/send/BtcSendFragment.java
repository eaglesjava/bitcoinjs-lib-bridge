package com.bitbill.www.ui.main.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.DrawableEditText;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.wallet.WalletModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by isanwenyu@163.com on 2017/12/9.
 */
public class BtcSendFragment extends BaseFragment<BtcSendMvpPresenter> implements BtcSendMvpView {

    @BindView(R.id.et_send_address)
    DrawableEditText etSendAddress;
    @BindView(R.id.btn_next)
    Button btnNext;
    @Inject
    BtcSendMvpPresenter<WalletModel, BtcSendMvpView> mBtcSendMvpPresenter;
    private String mSendAddress;

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
        etSendAddress.setOnRightDrawableClickListener(v -> {
            //跳转到联系人选择页面
            ContactSelectActivity.startForResult(BtcSendFragment.this);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_send;
    }


    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //跳转到发送金额界面
                getMvpPresenter().validateBtcAddress();
                break;
        }
    }

    @Override
    public String getSendAddress() {
        return mSendAddress;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContactSelectActivity.REQUEST_SELECT_CONTACT_CODE && resultCode == ContactSelectActivity.RESULT_SELECT_CONTACT_CODE && data != null) {
            Contact contact = (Contact) data.getSerializableExtra(AppConstants.EXTRA_CONTACT);
            if (contact != null) {

                mSendAddress = contact.getAddress();
                String walletId = contact.getWalletId();
                setSendAddress(contact.getContactName() + "(" + (StringUtils.isEmpty(walletId) ? mSendAddress : walletId) + ")");

            }
        }
    }
}
