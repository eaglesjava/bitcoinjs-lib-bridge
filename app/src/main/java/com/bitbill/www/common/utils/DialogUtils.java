/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.bitbill.www.R;


/**
 * Created by isanwenyu@163.com on 2017/7/19.
 */
public class DialogUtils {

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    /**
     * close dialog
     *
     * @param dialog
     */
    public static void close(Dialog dialog) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    /***
     * 获取一个dialog
     *
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }


    /**
     * 获取只显示信息的对话框
     *
     * @param context
     * @param message 消息
     * @return
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String message) {
        return getConfirmDialog(context, message, null);
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     *
     * @param context
     * @param message
     * @param onClickListener 确定点击监听事件
     * @return
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        return builder;
    }

    /**
     * 带确定跟取消点击事件的消息对话框
     *
     * @param context
     * @param message
     * @param onOkClickListener
     * @param onCancleClickListener
     * @return
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        return getConfirmDialog(context, message, "确定", onOkClickListener, "取消", onCancleClickListener);
    }

    /**
     * 自定义确定及取消文字的消息对话框
     *
     * @param context
     * @param message
     * @param okStr
     * @param onOkClickListener
     * @param cancelStr
     * @param onCancleClickListener
     * @return
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, String okStr, DialogInterface.OnClickListener onOkClickListener, String cancelStr, DialogInterface.OnClickListener onCancleClickListener) {
        return getConfirmDialog(context, null, message, okStr, onOkClickListener, cancelStr, onCancleClickListener);
    }

    /**
     * 确认对话框最终实现类
     *
     * @param context
     * @param title
     * @param message
     * @param okStr
     * @param onOkClickListener
     * @param cancelStr
     * @param onCancleClickListener
     * @return
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String message, String okStr, DialogInterface.OnClickListener onOkClickListener, String cancelStr, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message);
        if (!TextUtils.isEmpty(okStr) && onOkClickListener != null)
            builder.setPositiveButton(okStr, onOkClickListener);

        if (!TextUtils.isEmpty(cancelStr) && onCancleClickListener != null)
            builder.setNegativeButton(cancelStr, onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("确定", null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    /**
     * 多选弹出框
     *
     * @param context
     * @param title
     * @param arrays       数据源
     * @param checkedItems 默认选中的item
     * @param listener
     * @return
     */
    public static AlertDialog.Builder getMutilSelectDialog(Context context, String title, String[] arrays, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMultiChoiceItems(arrays, checkedItems, listener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("确定", null);
        return builder;
    }

    /**
     * 多选弹出框
     *
     * @param context
     * @param arrays
     * @param checkedItems
     * @param listener
     * @return
     */
    public static AlertDialog.Builder getMutilSelectDialog(Context context, String[] arrays, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
        return getMutilSelectDialog(context, "", arrays, checkedItems, listener);
    }

}


