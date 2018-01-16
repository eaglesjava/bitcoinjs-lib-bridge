package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.app.AppManager;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.EditTextWapper;
import com.bitbill.www.common.widget.dialog.BaseConfirmDialog;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class EditContactActivity extends BaseToolbarActivity<EditContactMvpPresenter> implements EditContactMvpView {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_top_content)
    TextView tvTopContent;
    @BindView(R.id.etw_contact_name)
    EditTextWapper etwContactName;
    @BindView(R.id.etw_contact_remark)
    EditTextWapper etwContactRemark;
    @Inject
    EditContactMvpPresenter<ContactModel, EditContactMvpView> mEditContactMvpPresenter;
    private Contact mContact;

    public static void start(Context context, Contact contact) {
        Intent starter = new Intent(context, EditContactActivity.class);
        starter.putExtra(AppConstants.EXTRA_CONTACT, contact);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {

        mContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_CONTACT);
    }

    @Override
    public EditContactMvpPresenter getMvpPresenter() {
        return mEditContactMvpPresenter;
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
        if (mContact != null) {
            if (StringUtils.isNotEmpty(mContact.getWalletId())) {
                tvTopTitle.setText(R.string.text_contact_wallet_id);
                tvTopContent.setText(mContact.getWalletId());
            } else if (StringUtils.isNotEmpty(mContact.getAddress())) {

                tvTopTitle.setText(R.string.text_contact_address);
                tvTopContent.setText(mContact.getAddress());
            } else {
                showMessage(R.string.msg_get_contact_info_fail);
            }
            etwContactName.setText(mContact.getContactName());
            etwContactRemark.setText(mContact.getRemark());
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_contact;
    }

    @Override
    public Contact getContact() {
        return mContact;
    }

    @Override
    public void updateContactSuccess() {
        showMessage(R.string.msg_update_contact_success);
        ContactUpdateEvent event = new ContactUpdateEvent();
        event.setData(mContact);
        EventBus.getDefault().postSticky(event);
        finish();

    }

    @Override
    public void updateContactFail(String message) {

        showMessage(StringUtils.isEmpty(message) ? getString(R.string.msg_update_contact_fail) : message);
    }

    @Override
    public String getContactName() {
        return etwContactName.getText();
    }

    @Override
    public String getRemark() {
        return etwContactRemark.getText();
    }

    @Override
    public void requireContactName() {
        showMessage(R.string.text_require_contact_name);
    }

    @Override
    public void requireContact() {

        showMessage(R.string.msg_get_contact_info_fail);
    }

    @Override
    public void contactNoEdit() {
        showMessage(R.string.msg_contact_info_no_edit);
    }

    @Override
    public void deleteContactSuccess() {
        showMessage(R.string.msg_delete_contact_success);
        EventBus.getDefault().postSticky(new ContactUpdateEvent());
        AppManager.get().finishActivity(ContactDetailActivity.class);
        finish();
    }

    @Override
    public void deleteContactFail(String message) {
        showMessage(StringUtils.isEmpty(message) ? getString(R.string.msg_delete_contact_fail) : message);
    }

    @OnClick({R.id.btn_save, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                getMvpPresenter().updateContact();
                break;
            case R.id.btn_delete:
                MessageConfirmDialog.newInstance("提示", "确定要删除该联系人吗？", "删除", false)
                        .setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                                    getMvpPresenter().deleteContact();
                                }
                            }
                        }).show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
                break;
        }
    }
}
