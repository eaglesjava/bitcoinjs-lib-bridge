package com.bitbill.www.ui.main.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.presenter.ValidateAddressMvpPresenter;
import com.bitbill.www.common.presenter.ValidateAddressMvpView;
import com.bitbill.www.common.utils.KeyboardStatusDetector;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.DrawableEditText;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.ui.main.contact.ContactSelectActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by isanwenyu@163.com on 2017/12/9.
 */
public class BtcSendFragment extends BaseFragment<BtcSendMvpPresenter> implements BtcSendMvpView, ValidateAddressMvpView {

    @BindView(R.id.et_send_address)
    DrawableEditText etSendAddress;
    @BindView(R.id.btn_next)
    Button btnNext;
    @Inject
    BtcSendMvpPresenter<ContactModel, BtcSendMvpView> mBtcSendMvpPresenter;
    @Inject
    ValidateAddressMvpPresenter<BtcModel, ValidateAddressMvpView> mValidateAddressMvpPresenter;
    private Contact mSendContact;
    private KeyboardStatusDetector mKeyboardStatusDetector;

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
        addPresenter(mValidateAddressMvpPresenter);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mKeyboardStatusDetector = new KeyboardStatusDetector();
        mKeyboardStatusDetector.registerFragment(this);
        mKeyboardStatusDetector.setKeyboardVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean keyboardVisible) {

                //清除之前的选择内容
                if (etSendAddress != null) {
                    if (keyboardVisible) {
                        etSendAddress.requestFocus();
                        if (StringUtils.isNotEmpty(etSendAddress.getText().toString())) {
                            etSendAddress.setText("");
                            mSendContact = null;
                        }
                    } else {
                        etSendAddress.clearFocus();
                    }
                }
            }
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
                if (mSendContact == null || StringUtils.isEmpty(mSendContact.getWalletId())) {
                    mValidateAddressMvpPresenter.validateBtcAddress();
                } else {
                    showLoading();
                    //请求最新联系人关联walletid地址
                    getMvpPresenter().getLastAddress();
                }
                break;
        }
    }

    @Override
    public String getSendAddress() {
        if (mSendContact != null) {

            return mSendContact.getAddress();
        }
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
            SendAmountActivity.start(getBaseActivity(), getSendAddress(), mSendContact);
        } else {
            onError(R.string.fail_invalid_address);
        }
    }

    @Override
    public void requireAddress() {
        onError(R.string.fail_send_address_null);

    }

    @Override
    public String getAddress() {
        return getSendAddress();
    }

    @Override
    public Contact getSendContact() {
        return mSendContact;
    }

    public void setSendContact(Contact sendContact) {
        mSendContact = sendContact;
        if (sendContact != null) {
            String walletId = sendContact.getWalletId();
            setSendAddress(sendContact.getContactName() + "(" + (StringUtils.isEmpty(walletId) ? sendContact.getAddress() : walletId) + ")");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContactSelectActivity.REQUEST_SELECT_CONTACT_CODE && resultCode == ContactSelectActivity.RESULT_SELECT_CONTACT_CODE && data != null) {
            Contact contact = (Contact) data.getSerializableExtra(AppConstants.EXTRA_CONTACT);
            setSendContact(contact);
        }
    }

    @Override
    public String getWalletId() {
        return mSendContact.getWalletId();
    }

    @Override
    public void getLastAddressSuccess(String address) {
        mSendContact.setAddress(address);
        mValidateAddressMvpPresenter.validateBtcAddress();
        getMvpPresenter().updateContact();
    }

    @Override
    public void getLastAddressFail() {
        onError(R.string.fail_get_contact_last_address);
    }

    @Override
    public void requireWalletId() {
        onError(R.string.fail_get_contact_info);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mKeyboardStatusDetector != null) {
            mKeyboardStatusDetector.unRegisterFragment(this);
        }
    }

    public void clearData() {
        setSendAddress(null);
        setSendContact(null);
    }
}
