package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.contact.network.entity.Contact;

import butterknife.BindView;

public class ContactDetailActivity extends BaseToolbarActivity {

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
        if (mContact == null || StringUtils.isEmpty(mContact.getName()) || StringUtils.isEmpty(String.valueOf(mContact.getName().charAt(0)))) {
            // TODO: 2017/12/20
            showMessage("加载联系人信息失败");
            return;
        }
        mTvContactLabel.setText(String.valueOf(mContact.getName().charAt(0)));
        mTvContactName.setText(mContact.getName());
        mTvWalletAddress.setText(mContact.getAddress());
        mTvWalletRemark.setText(mContact.getRemark());
    }

    @Override
    public void initData() {

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

}
