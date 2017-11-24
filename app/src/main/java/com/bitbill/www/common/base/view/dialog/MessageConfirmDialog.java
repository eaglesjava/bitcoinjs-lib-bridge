package com.bitbill.www.common.base.view.dialog;

import android.os.Bundle;
import android.widget.TextView;

import com.bitbill.www.R;

import butterknife.BindView;

/**
 * Created by isanwenyu@163.com on 2017/11/24.
 */
public class MessageConfirmDialog extends BaseConfirmDialog {
    public static final String TAG = "MessageConfirmDialog";
    private static final String CONFIRM_MESSAGE = "confirm_msg";
    @BindView(R.id.dialog_message)
    TextView dialogMessage;

    public static MessageConfirmDialog newInstance(String title, String msg, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(CONFIRM_MESSAGE, msg);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        MessageConfirmDialog fragment = new MessageConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static MessageConfirmDialog newInstance(String msg, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_MESSAGE, msg);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        MessageConfirmDialog fragment = new MessageConfirmDialog();
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
        dialogMessage.setText(getArguments().getString(CONFIRM_MESSAGE));
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm_message;
    }

}
