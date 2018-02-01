package com.bitbill.www.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.ui.main.MainActivity;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

public class JpushMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "JpushMessageReceiver";
    private static final int NOTIFICATION_SHOW_AT_MOST = 8;
    private static final String NOTIFICATION_JPUSH_CHANNEL_ID = "jpush_channel_id";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消息");
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的通知");

            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 实现自定义推送声音
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "processCustomMessage: extras----->" + extras);

        JPushCustomMessage customMessage = null;
        try {
            JSONObject jsonObject = new JSONObject(extras);
            if (jsonObject != null) {
                customMessage = JsonUtils.deserialize(jsonObject.optString("extra"), JPushCustomMessage.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (customMessage == null) {
            return;
        }
        String amount = customMessage.getAmount();
        if (StringUtils.isEmpty(amount)) {
            return;
        }
        msg = String.format(context.getString(R.string.msg_push_receive_amount), amount);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        String contentTitle = StringUtils.isEmpty(title) ? "Bitbill消息" : title;

        Intent mIntent = new Intent(context, MainActivity.class);
        // 刷新未确认列表
        mIntent.putExtra(AppConstants.EXTRA_LIST_UNCONFIRM, true);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

//        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.diaoluo_da);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder notification = new Notification.Builder(context, NOTIFICATION_JPUSH_CHANNEL_ID);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_JPUSH_CHANNEL_ID, "消息推送", NotificationManager.IMPORTANCE_DEFAULT);
//            //AudioAttributes是一个封装音频各种属性的类
//            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
//            //设置音频流的合适属性
//            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            channel.setSound(null, null);
            channel.enableLights(true);
            channel.enableVibration(true);
            notification.setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentIntent(pendingIntent)
                    .setContentText(msg)
                    .setTicker(contentTitle)
//                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setNumber(NOTIFICATION_SHOW_AT_MOST);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(NOTIFICATION_SHOW_AT_MOST, notification.build());  //id随意，正好使用定义的常量做id，0除外，0为默认的Notification

        } else {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentText(msg)
                    .setTicker(contentTitle)
//                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setNumber(NOTIFICATION_SHOW_AT_MOST)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS);
            notificationManager.notify(NOTIFICATION_SHOW_AT_MOST, notification.build());  //id随意，正好使用定义的常量做id，0除外，0为默认的Notification
        }

    }


    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
    }
}