package com.bitbill.www.ui.main.send;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseFragmentActivity;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.ui.main.contact.ContactFragment;

public class ContactSelectActivity extends BaseFragmentActivity {

    public static final int REQUEST_SELECT_CONTACT_CODE = 0x55;
    public static final int RESULT_SELECT_CONTACT_CODE = 0x66;

    public static void startForResult(BaseFragment fragment) {
        Intent starter = new Intent(fragment.getBaseActivity(), ContactSelectActivity.class);
        fragment.startActivityForResult(starter, REQUEST_SELECT_CONTACT_CODE);
    }

    @Override
    protected Fragment getFragment() {
        return ContactFragment.newInstance(true);
    }

    public void finishSelect(Contact contact) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.EXTRA_CONTACT, contact);
        setResult(RESULT_SELECT_CONTACT_CODE, intent);
        finish();

    }
}
