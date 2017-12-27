package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.EditTextWapper;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.eventbus.UpdateContactEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchContactResultActivity extends BaseToolbarActivity<SearchContactResultMvpPresenter> implements SearchContactResultMvpView {


    @BindView(R.id.etw_contact_name)
    EditTextWapper mEtwContactName;
    @BindView(R.id.etw_contact_remark)
    EditTextWapper mEtwContactRemark;
    @BindView(R.id.btn_confirm_add)
    Button mConfirmBtn;
    @Inject
    SearchContactResultMvpPresenter<ContactModel, SearchContactResultMvpView> mSearchContactResultMvpPresenter;
    private String mWalletId;
    private String mAddress;

    public static void start(Context context, String address, String walletId) {
        Intent starter = new Intent(context, SearchContactResultActivity.class);
        starter.putExtra(AppConstants.EXTRA_CONTACT_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_WALLET_ID, walletId);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mAddress = getIntent().getStringExtra(AppConstants.EXTRA_CONTACT_ADDRESS);
        mWalletId = getIntent().getStringExtra(AppConstants.EXTRA_WALLET_ID);

    }

    @Override
    public SearchContactResultMvpPresenter getMvpPresenter() {
        return mSearchContactResultMvpPresenter;
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
        setTitle(mWalletId);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_contact_result;
    }

    @Override
    public String getWalletId() {
        return mWalletId;
    }

    @Override
    public String getAddress() {
        return mAddress;
    }

    @Override
    public String getRemark() {
        return mEtwContactRemark.getText();
    }

    @Override
    public String getContactName() {
        return mEtwContactName.getText();
    }

    @Override
    public void addContactSuccess() {
        EventBus.getDefault().postSticky(new UpdateContactEvent());
        finish();
    }

    @Override
    public void addContactFail(String message) {
        showMessage(StringUtils.isEmpty(message) ? getString(R.string.fail_add_contact) : message);
    }

    @Override
    public void requireWalletId() {
        showMessage(R.string.msg_get_wallet_id_fail);
    }

    @Override
    public void requireContactName() {

        mEtwContactName.setError(R.string.msg_input_contact_name);
        mEtwContactName.requestFocus();
    }

    @Override
    public void isExsistContact() {
        showMessage("联系人已存在");
    }

    @OnClick(R.id.btn_confirm_add)
    public void onViewClicked() {

        mEtwContactName.removeError();
        getMvpPresenter().checkContact();
    }
}
