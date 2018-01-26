package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.widget.DrawableEditText;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.contact.ContactModel;

import javax.inject.Inject;

import butterknife.BindView;

public class AddContactByIdActivity extends BaseToolbarActivity<AddContactByIdMvpPresenter> implements AddContactByIdMvpView {

    @BindView(R.id.et_search_id)
    DrawableEditText etSearchId;
    @Inject
    AddContactByIdMvpPresenter<ContactModel, AddContactByIdMvpView> mAddContactByIdMvpPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AddContactByIdActivity.class);
        context.startActivity(starter);
    }

    @Override
    public AddContactByIdMvpPresenter getMvpPresenter() {
        return mAddContactByIdMvpPresenter;
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
        etSearchId.setOnRightDrawableClickListener(v -> searchWalletId());
        etSearchId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {   // 按下完成按钮，这里和上面imeOptions对应
                searchWalletId();
                return true;
            }
            return false;
        });

    }

    private void searchWalletId() {
        getMvpPresenter().searchWalletId();
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_contact_by_id;
    }


    @Override
    public String getWalletId() {
        return etSearchId.getText().toString();
    }

    @Override
    public void searchWalletIdSuccess() {
        // 跳转到搜索结果界面
        SearchContactResultActivity.start(AddContactByIdActivity.this, null, getWalletId());

    }

    @Override
    public void searchWalletIdFail() {
        MessageConfirmDialog.newInstance("钱包不存在", true).show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
    }

    @Override
    public void requireWalletId() {
        showMessage("ID不能为空");
    }
}
