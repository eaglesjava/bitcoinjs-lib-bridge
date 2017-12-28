package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.dialog.PwdDialogFragment;
import com.bitbill.www.model.eventbus.WalletDeleteEvent;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.ui.wallet.backup.BackUpWalletActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletDetailActivity extends BaseToolbarActivity<WalletDetailMvpPresenter> implements WalletDetailMvpView {


    @BindView(R.id.iv_wallet_id_qrcode)
    ImageView mIvQrcode;
    @BindView(R.id.tv_wallet_id)
    TextView mTvWalletId;
    @BindView(R.id.tv_wallet_add_time)
    TextView mTvWalletAddTime;
    @BindView(R.id.btn_wallet_backup)
    Button mBtnBackup;
    @Inject
    WalletDetailMvpPresenter<WalletModel, WalletDetailMvpView> mWalletDetailMvpPresenter;
    private String mWalletId;
    private Wallet mWallet;
    private PwdDialogFragment mPwdDialogFragment;

    public static void start(Context context, Wallet wallet) {
        Intent starter = new Intent(context, WalletDetailActivity.class);
        starter.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
    }

    @Override
    public WalletDetailMvpPresenter getMvpPresenter() {
        return mWalletDetailMvpPresenter;
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
        if (mWallet != null) {
            mTvWalletId.setText(mWallet.getName());
            setTitle(mWallet.getName());
            mTvWalletAddTime.setText(StringUtils.formatDate(mWallet.getCreatedAt()));
            if (mWallet.getIsBackuped()) {
                mBtnBackup.setVisibility(View.GONE);
            }
        }
        mPwdDialogFragment = PwdDialogFragment.newInstance(getString(R.string.title_delete_wallet), getString(R.string.msg_delete_wallet), mWallet, false);
        mPwdDialogFragment.setOnPwdValidatedListener(new PwdDialogFragment.OnPwdValidatedListener() {
            @Override
            public void onPwdCnfirmed(String confirmPwd) {
                getMvpPresenter().deleteWallet();
            }

            @Override
            public void onDialogCanceled() {

            }
        });
    }

    @Override
    public void initData() {
        if (mWallet != null) {
            mWalletId = mWallet.getName();
        }
        getMvpPresenter().createWalletIdQrcode();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_detail;
    }


    @OnClick({R.id.btn_wallet_backup, R.id.btn_wallet_address, R.id.btn_wallet_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wallet_backup:
                //跳转到备份功能
                BackUpWalletActivity.start(WalletDetailActivity.this, mWallet, true);
                break;
            case R.id.btn_wallet_address:
                // TODO: 2017/12/28  跳转到钱包地址界面
                showMessage("钱包地址界面");
                break;
            case R.id.btn_wallet_delete:
                //删除钱包
                showPwdDialog();
                break;
        }
    }

    private void showPwdDialog() {
        mPwdDialogFragment.show(getSupportFragmentManager(), PwdDialogFragment.TAG);
    }

    @Override
    public String getWalletId() {
        return mWalletId;
    }

    @Override
    public void createQrcodeSuccess(Bitmap bitmap) {
        mIvQrcode.setImageBitmap(bitmap);
    }

    @Override
    public void createQrcodeFail() {
        showMessage("生成钱包ID二维码失败");
    }

    @Override
    public Wallet getWallet() {
        return mWallet;
    }

    @Override
    public void deleteWalletSuccess() {
        showMessage(R.string.msg_delete_wallet_success);
        EventBus.getDefault().postSticky(new WalletDeleteEvent(getWallet()));
        finish();
    }

    @Override
    public void deleteWalletFail() {
        showMessage(R.string.msg_delete_wallet_fail);
    }

    @Override
    public void getWalletInfoFail() {
        showMessage(R.string.msg_get_wallet_info_fail);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletUpdateEvent(WalletUpdateEvent walletUpdateEvent) {
        Wallet wallet = walletUpdateEvent.getWallet();
        if (wallet != null) {
            if (wallet.equals(getWallet())) {
                //关闭备份按钮
                mBtnBackup.setVisibility(View.GONE);
            }
        }
    }
}
