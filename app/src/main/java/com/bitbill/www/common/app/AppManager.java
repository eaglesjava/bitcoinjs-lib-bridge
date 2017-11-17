/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.app;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Created by isanwenyu@163.com on 2017/7/19.
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * Get single instance
     */
    public static AppManager get() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * Get the specified Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * Add Activity to the stack
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * Get the current Activity (the last one in the stack)
     */
    public Activity currentActivity() {
        if (activityStack == null)
            return null;
        try {
            Activity activity = activityStack.lastElement();
            return activity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the current Activity (the last one in the stack)
     */
    public void finishActivity() {
        if (activityStack == null)
            return;
        try {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * finish specified Activity
     */
    public void finishActivity(Activity activity) {
//        if (activity != null && !activity.isFinishing()) {
        if (activity != null) {//
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * finish specified classname Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * Finish specified activity and all above it in stack
     */
    public void finishOtherActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = activityStack.size() - 1; i > 0; i--) {
                Activity activity = activityStack.get(i);
                if (null != activity && !activity.getClass().equals(cls)) {
                    finishActivity(activity);
                } else {
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * finish all Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                //finishActivity方法中的activity.isFinishing()方法会导致某些activity无法销毁
                //貌似跳转的时候最后一个activity 是finishing状态，所以没有执行
                //内部实现不是很清楚，但是实测结果如此，使用下面代码则没有问题
                // find by TopJohn
                //finishActivity(activityStack.get(i));

                activityStack.get(i).finish();
                //break;
            }
        }
        activityStack.clear();
    }

    /**
     * application exit
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            // kill process
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
