package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseFragmentActivity;
import com.bitbill.www.model.eventbus.WalletDeleteEvent;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by isanwenyu on 2017/12/27.
 */

public class WalletSettingActivity extends BaseFragmentActivity {

    private WalletSettingFragment mWalletSettingFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, WalletSettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        setTitle(R.string.title_activity_wallet_setting);
    }

    @Override
    protected Fragment getFragment() {
        mWalletSettingFragment = WalletSettingFragment.newInstance();
        return mWalletSettingFragment;
    }

    /**
     * todo resume时会多次调用需优化
     *
     * @param walletUpdateEvent
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletUpdateSuccess(WalletUpdateEvent walletUpdateEvent) {
        Wallet wallet = walletUpdateEvent.getWallet();
        //重新加载钱包信息
        if (mWalletSettingFragment != null) {
            mWalletSettingFragment.updateWallet(wallet);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletDeleteSuccess(WalletDeleteEvent walletDeleteEvent) {
        Wallet wallet = walletDeleteEvent.getWallet();
        //重新加载钱包信息
        if (mWalletSettingFragment != null) {
            mWalletSettingFragment.deleteWallet(wallet);
        }
    }
}
