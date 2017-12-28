package com.bitbill.www.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bitbill.www.app.BitbillApp;

/**
 * <pre>
 * 获取设备信息工具
 * Created by zhuyuanbao on 2016/3/1.
 * Copyright (c) 2016 www.zhengshijr.com. All rights reserved.
 * </pre>
 */
public class DeviceUtil {

    // Storage Permissions variables
    public static final int REQUEST_EXTERNAL_STORAGE = 0x01;
    // camera and read phone state Permissions variables
    public static final int REQUEST_EXTERNAL_CAMERA = 0x02;
    private static final java.lang.String TAG = "DeviceUtil";
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };
    public static String[] PERMISSIONS_CHANGE_HEADER_IMG = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 判断方法是否兼容KITKAT Api19 android4.4
     */
    public static boolean isMethodsCompatKITKAT() {
        return isMethodsCompat(Build.VERSION_CODES.KITKAT);
    }

    /**
     * 判断方法是否兼容KITKAT Api21 android5.0
     *
     * @return
     */
    public static boolean isMethodsCompatLOLLIPOP() {
        return isMethodsCompat(Build.VERSION_CODES.LOLLIPOP);
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return tm.getDeviceId();
        }
        return "";
    }

    // 获取设备id
    public static String getDeviceId() {
        String id = Settings.Secure.getString(BitbillApp.get().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }

    // 获得设备型号
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * device factory name, e.g: Samsung
     *
     * @return the vENDOR
     */
    public static String getVendor() {
        return Build.BRAND;
    }

    /**
     * @return the OS version
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Retrieves application's version number from the manifest
     *
     * @return versionName
     */
    public static String getAppVersion() {
        String version = "0.0.0";
        try {
            PackageInfo packageInfo = BitbillApp.get().getPackageManager().getPackageInfo(
                    BitbillApp.get().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取int类型的版本号
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = BitbillApp.get()
                    .getPackageManager()
                    .getPackageInfo(BitbillApp.get().getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * android6.0存储权限验证
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * android6.0拍照权限验证
     *
     * @param activity
     * @return true:验证成功 已有权限 false:验证权限失败
     */
    public static boolean verifyCameraPermissions(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        }
        // Check if we have camera permission
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int readPhonePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);

        if (cameraPermission != PackageManager.PERMISSION_GRANTED || readPhonePermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CAMERA,
                    REQUEST_EXTERNAL_CAMERA
            );
            return false;
        } else {
            return true;
        }
    }

    /**
     * 友盟 分享 请求 权限
     *
     * @param activity
     * @return
     */
    public static boolean verifyUmengSharePermissions(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        }
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(activity, mPermissionList, 100);
        return true;
    }

    /**
     * 更换头像权限
     * 相机 存储
     *
     * @param activity
     * @return
     */
    public static boolean verifyChangeHeaderImgPermissions(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        }
        ActivityCompat.requestPermissions(activity, PERMISSIONS_CHANGE_HEADER_IMG, REQUEST_EXTERNAL_CAMERA);
        return true;
    }

    /**
     * @return 友盟渠道名称字符串 如果为空返回空串
     */
    public static String getApkUMChannel() {

        ApplicationInfo appInfo = null;
        String channel = null;
        try {
            appInfo = BitbillApp.get().getPackageManager()
                    .getApplicationInfo(BitbillApp.get().getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getApkMetaData:UMENG_CHANNEL:" + channel);
        return StringUtils.isNotEmpty(channel) ? channel : "";
    }
}
