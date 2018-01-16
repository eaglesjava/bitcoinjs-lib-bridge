package com.bitbill.www.ui.main.contact;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.EditTextWapper;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;
import com.bitbill.www.ui.main.send.ScanQrcodeActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBtcContactByAddressFragment extends BaseLazyFragment<AddContactByAddressMvpPresenter> implements AddContactByAddressMvpView {

    public static final String TAG = "AddBtcContactByAddressFragment";
    @BindView(R.id.etw_contact_name)
    EditTextWapper mEtwContactName;
    @BindView(R.id.etw_contact_address)
    EditTextWapper mEtwContactAddress;
    @BindView(R.id.etw_contact_remark)
    EditTextWapper mEtwContactRemark;
    @Inject
    AddContactByAddressMvpPresenter<ContactModel, AddContactByAddressMvpView> mAddContactByAddressMvpPresenter;
    private boolean cancel;
    private View focusView;

    public AddBtcContactByAddressFragment() {
        // Required empty public constructor
    }

    public static AddBtcContactByAddressFragment newInstance(String address) {

        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_ADDRESS, address);
        AddBtcContactByAddressFragment fragment = new AddBtcContactByAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact_save:
                selectSave();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void selectSave() {

        mEtwContactAddress.removeError();
        mEtwContactName.removeError();

        cancel = false;
        focusView = null;

        getMvpPresenter().checkContact();

        if (cancel) {
            // There was an error; don't attempt init and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

    }

    @Override
    public AddContactByAddressMvpPresenter getMvpPresenter() {
        return mAddContactByAddressMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onBeforeSetContentLayout() {
        setHasOptionsMenu(true);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mEtwContactAddress.setOnRightImageClickListener(v -> ScanQrcodeActivity.start(getBaseActivity(), AddBtcContactByAddressFragment.TAG));
    }

    @Override
    public void initData() {
        String address = getArguments().getString(AppConstants.ARG_ADDRESS);
        mEtwContactAddress.setText(address);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_btc_contact_by_address;
    }

    @Override
    public void lazyData() {

    }

    @Override
    public void isExsistContact() {
        showMessage(R.string.msg_contact_is_exsist);
    }

    @Override
    public String getAddress() {
        return mEtwContactAddress.getText();
    }

    public void setAddress(String address) {
        if (mEtwContactAddress != null) {
            mEtwContactAddress.setText(address);
        }
    }

    @Override
    public String getContactName() {
        return mEtwContactName.getText();
    }

    @Override
    public void requireContactName() {
        mEtwContactName.setError(R.string.error_contact_name_required);
        focusView = mEtwContactName;
        cancel = true;
    }

    @Override
    public void requireAddress() {
        mEtwContactAddress.setError(R.string.error_contact_address_required);
        focusView = mEtwContactAddress;
        cancel = true;
    }

    @Override
    public String getRemark() {
        return mEtwContactRemark.getText();
    }

    @Override
    public void addContactSuccess() {
        EventBus.getDefault().postSticky(new ContactUpdateEvent());
        getBaseActivity().finish();

    }

    @Override
    public void addContactFail(String message) {
        showMessage(StringUtils.isEmpty(message) ? getString(R.string.fail_add_contact) : message);
    }
}
