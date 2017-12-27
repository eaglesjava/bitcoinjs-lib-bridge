package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.presenter.GetLastAddressMvpPresenter;
import com.bitbill.www.common.presenter.GetLastAddressMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;

import javax.inject.Inject;

import butterknife.BindView;

public class ContactDetailActivity extends BaseToolbarActivity<GetLastAddressMvpPresenter> implements GetLastAddressMvpView {

    @BindView(R.id.tv_contact_label)
    TextView mTvContactLabel;
    @BindView(R.id.tv_contact_name)
    TextView mTvContactName;
    @BindView(R.id.tv_wallet_id)
    TextView mTvWalletId;
    @BindView(R.id.tv_wallet_address)
    TextView mTvWalletAddress;
    @BindView(R.id.tv_wallet_remark)
    TextView mTvWalletRemark;
    @BindView(R.id.ll_wallet_id)
    LinearLayout mLLWalletId;
    @BindView(R.id.ll_wallet_address)
    LinearLayout mLLWalletAddress;
    @Inject
    GetLastAddressMvpPresenter<ContactModel, GetLastAddressMvpView> mGetLastAddressMvpPresenter;
    private Contact mContact;

    public static void start(Context context, Contact contact) {
        Intent starter = new Intent(context, ContactDetailActivity.class);
        starter.putExtra(AppConstants.EXTRA_CONTACT, contact);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_CONTACT);
    }

    @Override
    public GetLastAddressMvpPresenter getMvpPresenter() {
        return mGetLastAddressMvpPresenter;
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
        if (mContact == null || StringUtils.isEmpty(mContact.getContactName()) || StringUtils.isEmpty(String.valueOf(mContact.getContactName().charAt(0)))) {
            showMessage("加载联系人信息失败");
            return;
        }
        mTvContactLabel.setText(StringUtils.getNameLabel(mContact.getContactName()));
        mTvContactName.setText(mContact.getContactName());
        if (StringUtils.isEmpty(mContact.getAddress())) {
            mLLWalletAddress.setVisibility(View.GONE);
        } else {
            mTvWalletAddress.setText(mContact.getAddress());
        }
        mTvWalletRemark.setText(StringUtils.isEmpty(mContact.getRemark()) ? "无" : mContact.getRemark());
        if (StringUtils.isEmpty(mContact.getWalletId())) {
            mLLWalletId.setVisibility(View.GONE);
        } else {
            mTvWalletId.setText(mContact.getWalletId());
        }
        mTvWalletAddress.setText(mContact.getAddress());
    }

    @Override
    public void initData() {
        getMvpPresenter().getLastAddress();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact_edit) {
            // TODO: 2017/12/20 跳转到编辑界面
            showMessage("编辑联系人");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getWalletId() {
        return mContact == null ? "" : mContact.getWalletId();
    }

    @Override
    public void getLastAddressSuccess(String address) {

        mTvWalletAddress.setText(mContact.getAddress());
    }

    @Override
    public void getLastAddressFail() {

    }

    @Override
    public void requireWalletId() {

    }
}
