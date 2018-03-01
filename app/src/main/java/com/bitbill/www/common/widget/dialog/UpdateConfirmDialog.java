package com.bitbill.www.common.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

import com.bitbill.www.R;

import butterknife.BindView;

/**
 * Created by isanwenyu@163.com on 2017/11/24.
 */
public class UpdateConfirmDialog extends BaseConfirmDialog {
    public static final String TAG = "MessageConfirmDialog";
    private static final String CONFIRM_MESSAGE = "confirm_msg";
    private static final String CONFIRM_CANCEL = "cancelable";
    @BindView(R.id.dialog_message)
    TextView dialogMessage;

    public static UpdateConfirmDialog newInstance(String title, String msg, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(CONFIRM_MESSAGE, msg);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        UpdateConfirmDialog fragment = new UpdateConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdateConfirmDialog newInstance(String title, String msg, String positiveText, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(CONFIRM_MESSAGE, msg);
        args.putString(CONFIRM_POSITIVE_BTN_TEXT, positiveText);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        UpdateConfirmDialog fragment = new UpdateConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdateConfirmDialog newInstance(String title, String msg, String positiveText, boolean isOnlyPositiveBtn, boolean cancel) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(CONFIRM_MESSAGE, msg);
        args.putString(CONFIRM_POSITIVE_BTN_TEXT, positiveText);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        args.putBoolean(CONFIRM_CANCEL, cancel);
        UpdateConfirmDialog fragment = new UpdateConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdateConfirmDialog newInstance(String msg, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_MESSAGE, msg);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        UpdateConfirmDialog fragment = new UpdateConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        dialogMessage.setMovementMethod(new ScrollingMovementMethod());
        dialogMessage.setText(Html.fromHtml(getArguments().getString(CONFIRM_MESSAGE)));
        boolean cancelable = getArguments().getBoolean(CONFIRM_CANCEL, true);
        if (!cancelable) {
            getDialog().setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH;
                }
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_update_confirm_message;
    }

    public void show(FragmentManager supportFragmentManager) {
        show(supportFragmentManager, TAG);
    }
}
