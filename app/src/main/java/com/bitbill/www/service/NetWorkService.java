/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.bitbill.www.R;
import com.bitbill.www.common.utils.NetworkUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.entity.eventbus.NetWorkChangedEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */
public class NetWorkService extends Service {

    private final String TAG = "NetWorkService";
    private NetWorkReceiver mReceiver;
    private String mNetWorkType;

    public static void start(Context context) {
        Intent starter = new Intent(context, NetWorkService.class);
        context.startService(starter);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "create NetWorkService");
        //Initialize default network state
        mNetWorkType = NetworkUtils.getNetworkType(getApplicationContext());
        mReceiver = new NetWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class NetWorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                String networkType = NetworkUtils.getNetworkType(getApplicationContext());
                // Not the same as the initial state network or the last network state
                if (!networkType.equals(mNetWorkType)) {
                    mNetWorkType = networkType;
                    if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                        String msg = null;
                        if (!NetworkUtils.NETWORK_UNKNOWN.equals(networkType)) {
                            //If the current network type is known
                            if (StringUtils.isNotEmpty(mNetWorkType)) {
                                //If there are no network before
                                msg = String.format(getString(R.string.toast_using_network_type), networkType);
                            } else {
                                msg = String.format(getString(R.string.toast_switch_network_type), networkType);
                            }
                        }
                        EventBus.getDefault().postSticky(new NetWorkChangedEvent(NetWorkChangedEvent.NETWORK_AVAILABLE, StringUtils.isNotEmpty(msg) ? msg : getString(R.string.toast_network_available)));
                    } else {
                        EventBus.getDefault().postSticky(new NetWorkChangedEvent(NetWorkChangedEvent.NETWORK_NOT_AVAILABLE, getString(R.string.toast_network_no_connection)));
                    }
                }
            }
        }
    }
}
