package com.bitbill.www.common.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Properties;


/**
 * 应用未能捕获的异常处理
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    private static CrashHandler crash;
    private static Thread.UncaughtExceptionHandler mHandler;
    private static Context context;
    private Properties deviceInfo = new Properties();

    private CrashHandler() {
    }

    public static CrashHandler getSingleCrash() {
        if (crash == null) {
            crash = new CrashHandler();
        }
        return crash;
    }

    public void init(Context cont) {
        context = cont;
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 当出现未捕获的异常时会调用这个方法
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handlException(ex) && mHandler != null) {
            mHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 重启程序，当程序崩溃时，将程序先退出再进行重启
            BitbillApp.get().restartApp();
        }

    }

    // 处理异常信息
    private boolean handlException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                Toast toast = Toast.makeText(context, R.string.error_app_exception, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);//居中显示
                toast.show();

                Looper.loop();
            }
        }.start();
        final String msg = ex.getLocalizedMessage();

        collectDeviceInfo();
//		saveInfoFile(ex);
        return true;
    }

    // 收集设备信息
    private void collectDeviceInfo() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                deviceInfo.put(VERSION_NAME, pi.versionName == null ? "not set"
                        : pi.versionName);
                deviceInfo.put(VERSION_CODE, pi.versionCode + "");
            }
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                deviceInfo.put(field.getName() + "", field.get(null) + "");
                Log.i(TAG, "device info:" + field.getName() + ":"
                        + field.get(null));
            }
        } catch (Exception e) {
            Log.i(TAG, "collect device info error");
            e.printStackTrace();
        }
    }

    /**
     * 保存到异常信息到本地文件
     *
     * @param ex
     */

    private void saveInfoFile(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            ex.printStackTrace(pw);
            cause = cause.getCause();
        }
        String info = writer.toString();
        Log.i(TAG, "deviceInfo:" + info);
        pw.close();
        deviceInfo.put(STACK_TRACE, info);
        long time = System.currentTimeMillis();
        String fileName = "crash-" + time + CRASH_REPORTER_EXTENSION;
        try {
            FileOutputStream out = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            deviceInfo.store(out, null);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
