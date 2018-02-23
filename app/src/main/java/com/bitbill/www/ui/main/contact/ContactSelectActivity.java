package com.bitbill.www.ui.main.contact;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseFragmentActivity;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.eventbus.ContactUpdateEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ContactSelectActivity extends BaseFragmentActivity {

    public static final int REQUEST_SELECT_CONTACT_CODE = 0x55;
    public static final int RESULT_SELECT_CONTACT_CODE = 0x66;
    private ContactFragment mContactFragment;

    public static void startForResult(BaseFragment fragment) {
        Intent starter = new Intent(fragment.getBaseActivity(), ContactSelectActivity.class);
        fragment.startActivityForResult(starter, REQUEST_SELECT_CONTACT_CODE);
    }

    @Override
    public void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        setTitle(R.string.title_activity_contact_select);
    }

    @Override
    protected Fragment getFragment() {
        mContactFragment = ContactFragment.newInstance(true);
        return mContactFragment;
    }

    public void finishSelect(Contact contact) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.EXTRA_CONTACT, contact);
        setResult(RESULT_SELECT_CONTACT_CODE, intent);
        finish();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateContactEvent(ContactUpdateEvent contactUpdateEvent) {
        //重新加载联系人信息
        if (mContactFragment != null) {
            mContactFragment.initData();
        }
    }

}
