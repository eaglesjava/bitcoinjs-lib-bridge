package com.bitbill.www.ui.main.send;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseTabsLazyFragment;
import com.bitbill.www.model.contact.db.entity.Contact;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFragment extends BaseTabsLazyFragment {

    public static final String TAG = SendFragment.class.getSimpleName();
    private BtcSendFragment mBtcSendFrg;

    public SendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SendFragment.
     */
    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.send_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_scan) {
            //打开扫描二维码
            ScanQrcodeActivity.start(getBaseActivity(), SendFragment.TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        setHasOptionsMenu(true);

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }


    @Override
    protected boolean isBlue() {
        return false;
    }

    @Override
    protected BaseFragment getBtcFragment() {
        return mBtcSendFrg = BtcSendFragment.newInstance();
    }

    @Override
    public void initData() {

    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {

        Log.d(TAG, "lazyData() called");
    }

    public void sendSuccess() {
        //reset ui
        if (mBtcSendFrg != null) {
            mBtcSendFrg.sendSuccess();
        }
    }

    public void setSendAddress(Contact sendContact) {
        if (mBtcSendFrg != null) {
            mBtcSendFrg.setSendContact(sendContact);
        }
    }

    public void setSendAddress(String sendAddress) {
        if (mBtcSendFrg != null) {
            mBtcSendFrg.setSendAddress(sendAddress);
        }
    }

    public void clearData() {
        if (mBtcSendFrg != null) {
            mBtcSendFrg.clearData();
        }
    }
}
