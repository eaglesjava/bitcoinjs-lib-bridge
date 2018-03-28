package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseTabsLazyFragment;
import com.bitbill.www.common.widget.SelectWalletView;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiveFragment extends BaseTabsLazyFragment<ReceiveMvpPresenter> {

    private static final String TAG = "ReceiveFragment";
    @BindView(R.id.wv_select)
    SelectWalletView selectWalletView;
    @Inject
    ReceiveMvpPresenter<AppModel, ReceiveMvpView> mReceiveMvpPresenter;
    private BtcReceiveFragment mBtcReceiveFragment;
    private Wallet mSelectedWallet;

    public ReceiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReceiveFragment.
     */
    public static ReceiveFragment newInstance() {
        ReceiveFragment fragment = new ReceiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public ReceiveMvpPresenter getMvpPresenter() {
        return mReceiveMvpPresenter;
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
        super.initView();
        selectWalletView.setOnWalletClickListener(new SelectWalletView.OnWalletClickListener() {
            @Override
            public void onWalletClick(Wallet wallet, View view) {
                if (isAdded()) {

                    ((MainActivity) getActivity()).showSelectWallet();
                }

            }

            @Override
            public void onBackupClick(Wallet wallet, View view) {
                //跳转到备份钱包界面
                BackUpWalletActivity.start(getBaseActivity(), wallet, false);
            }
        });


    }

    @Override
    protected boolean isBlue() {
        return false;
    }


    @Override
    protected BaseFragment getBtcFragment() {
        return mBtcReceiveFragment = BtcReceiveFragment.newInstance();
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_receive;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!getMvpPresenter().isRemindDialogShown()) {
                MessageConfirmDialog.newInstance(getString(R.string.dialog_title_friendly_remind),
                        getString(R.string.dialog_msg_change_address),
                        getString(R.string.dialog_btn_known),
                        true)
                        .show(getChildFragmentManager(), MessageConfirmDialog.TAG);
                getMvpPresenter().setRemindDialogShown();
            }
        }
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {
        loadBtcAddress();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.receive_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            // 刷新接收地址
            refreshBtcAddress();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshBtcAddress() {
        if (mBtcReceiveFragment != null) {
            mBtcReceiveFragment.refreshAddress(mSelectedWallet);
        }
    }

    private void loadBtcAddress() {
        if (mBtcReceiveFragment != null) {
            mBtcReceiveFragment.loadAddress(mSelectedWallet);
        }
    }

    public void setSelectedWallet(Wallet wallet) {
        if (selectWalletView != null) {
            selectWalletView.setVisibility(wallet == null ? View.VISIBLE : View.VISIBLE);
            if (wallet == null) {
                return;
            }
            mSelectedWallet = wallet;
            //刷新选择布局
            selectWalletView.setWallet(wallet);
        }
    }
}
