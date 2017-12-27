package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bitbill.www.common.base.view.BaseFragmentActivity;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;

import org.greenrobot.eventbus.EventBus;
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
    protected Fragment getFragment() {
        mWalletSettingFragment = WalletSettingFragment.newInstance();
        return mWalletSettingFragment;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onWalletUpdateSuccess(WalletUpdateEvent walletUpdateEvent) {
        WalletUpdateEvent stickyEvent = EventBus.getDefault().removeStickyEvent(WalletUpdateEvent.class);
        //重新加载钱包信息
        if (mWalletSettingFragment != null) {
            mWalletSettingFragment.initData();
        }
    }
}
