package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseTabsActivity;
import com.bitbill.www.common.utils.StringUtils;

public class AddContactByAddressActivity extends BaseTabsActivity {

    private String mAddress;
    private AddBtcContactByAddressFragment mAddBtcContactByAddressFragment;

    public static void start(Context context, String address) {
        Intent starter = new Intent(context, AddContactByAddressActivity.class);
        starter.putExtra(AppConstants.EXTRA_ADDRESS, address);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mAddress = intent.getStringExtra(AppConstants.EXTRA_ADDRESS);
        if (StringUtils.isNotEmpty(mAddress) && mAddBtcContactByAddressFragment != null) {
            mAddBtcContactByAddressFragment.setAddress(mAddress);
        }
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
        setTitle(R.string.title_activity_add_contact_by_address);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected BaseFragment getBtcFragment() {
        mAddBtcContactByAddressFragment = AddBtcContactByAddressFragment.newInstance(mAddress);
        return mAddBtcContactByAddressFragment;
    }


    @Override
    public void initData() {

    }
}
