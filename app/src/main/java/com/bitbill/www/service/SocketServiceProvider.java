package com.bitbill.www.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.SoundUtils;
import com.bitbill.www.di.component.DaggerServiceComponent;
import com.bitbill.www.di.component.ServiceComponent;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.eventbus.ConfirmedEvent;
import com.bitbill.www.model.eventbus.RegisterEvent;
import com.bitbill.www.model.eventbus.SocketServerStateEvent;
import com.bitbill.www.model.eventbus.UnConfirmEvent;
import com.bitbill.www.model.wallet.network.socket.Confirmed;
import com.bitbill.www.model.wallet.network.socket.Register;
import com.bitbill.www.model.wallet.network.socket.UnConfirmed;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.bitbill.www.app.AppConstants.EVENT_CONFIRM;
import static com.bitbill.www.app.AppConstants.EVENT_REGISTER;
import static com.bitbill.www.app.AppConstants.EVENT_UNCONFIRM;

public class SocketServiceProvider extends Service {
    private static final String TAG = "SocketServiceProvider";
    public static SocketServiceProvider instance = null;
    private final IBinder myBinder = new LocalBinder();
    @Inject
    Socket mSocket;
    @Inject
    AppModel mAppModel;
    private BitbillApp mBitbillApp;
    private ServiceComponent mServiceComponent;
    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "EVENT_REGISTER called with: args = [" + args + "]");
        }
    };
    private Emitter.Listener onConfirm = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Confirmed confirmed = null;
            try {
                JSONObject result = new JSONObject(String.valueOf(args[0]));
                Log.d(TAG, "EVENT_CONFIRM called with: args = [" + result + "]");
                confirmed = JsonUtils.deserialize(result.toString(), Confirmed.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  获取未确认列表
            EventBus.getDefault().postSticky(new ConfirmedEvent().setData(confirmed));
        }
    };
    private Emitter.Listener onUnConfirm = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            UnConfirmed unConfirmed = null;
            try {
                JSONObject result = new JSONObject(String.valueOf(args[0]));
                Log.d(TAG, "EVENT_UNCONFIRM called with: args = [" + result + "]");
                unConfirmed = JsonUtils.deserialize(result.toString(), UnConfirmed.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (unConfirmed != null && unConfirmed.getContext() != null) {
                if (AppConstants.TYPE_RECEIVE.equalsIgnoreCase(unConfirmed.getContext().getType())) {
                    //  根据设置开启音效
                    if (mAppModel.isSoundEnable()) {
                        // 播放声音
                        SoundUtils.playSound(R.raw.diaoluo_da);
                    }
                }
            }
            //  获取未确认列表
            EventBus.getDefault().postSticky(new UnConfirmEvent().setData(unConfirmed));
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "EVENT_CONNECT called with: args = [" + args + "]");
            EventBus.getDefault().postSticky(
                    new SocketServerStateEvent(SocketServerStateEvent.ServerState.connected));

        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "EVENT_DISCONNECT called with: args = [" + args + "]");
            EventBus.getDefault().postSticky(
                    new SocketServerStateEvent(SocketServerStateEvent.ServerState.disConnect));

        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            EventBus.getDefault().postSticky(
                    new SocketServerStateEvent(SocketServerStateEvent.ServerState.connectError)
            );
        }
    };
    private Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "EVENT_CONNECT_TIMEOUT called with: args = [" + args + "]");
            EventBus.getDefault().postSticky(
                    new SocketServerStateEvent(SocketServerStateEvent.ServerState.connectTimeout)
            );
        }
    };

    public static boolean isInstanceCreated() {
        return instance != null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SocketServiceProvider.class);
        context.startService(starter);
    }

    public void stop() {
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        return myBinder;
    }

    @Override
    public void onCreate() {
        if (isInstanceCreated()) {
            return;
        }
        super.onCreate();
        mBitbillApp = BitbillApp.get();
        mServiceComponent = DaggerServiceComponent.builder().applicationComponent(mBitbillApp.getComponent())
                .build();
        mServiceComponent.inject(this);
        initSocket();
        EventBus.getDefault().register(this);
        Log.d(TAG, "onCreate() called");
    }

    private void initSocket() {

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
                .on(Socket.EVENT_DISCONNECT, onDisconnect)
                .on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)
                .on(EVENT_REGISTER, onRegister)
                .on(EVENT_CONFIRM, onConfirm)
                .on(EVENT_UNCONFIRM, onUnConfirm);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        if (isInstanceCreated()) {
            return START_STICKY_COMPATIBILITY;
        }
        super.onStartCommand(intent, flags, startId);
        connectConnection();
        return START_STICKY;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketServerStateEvent(RegisterEvent socketServerStateEvent) {
        Register register = (Register) socketServerStateEvent.getData();
        registerWallet(register);
    }

    public void registerWallet(Register register) {
        mSocket.emit(EVENT_REGISTER, register.jsonString());
        Log.d(TAG, "registerWallet() called with: register = [" + register.jsonString() + "]");
    }

    private void connectConnection() {
        instance = this;
        mSocket.connect();
    }

    private void disconnectConnection() {
        instance = null;
        mSocket.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.off(Socket.EVENT_CONNECT, onConnect)
                .off(Socket.EVENT_DISCONNECT, onDisconnect)
                .off(Socket.EVENT_CONNECT_ERROR, onConnectError)
                .off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)
                .off(EVENT_REGISTER, onRegister)
                .off(EVENT_CONFIRM, onConfirm)
                .off(EVENT_UNCONFIRM, onUnConfirm);

        disconnectConnection();
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onDestroy() called");
    }

    public class LocalBinder extends Binder {
        public SocketServiceProvider getService() {
            return SocketServiceProvider.this;
        }
    }

}