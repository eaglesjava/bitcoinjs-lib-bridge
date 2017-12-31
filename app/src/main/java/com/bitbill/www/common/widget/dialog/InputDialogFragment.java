package com.bitbill.www.common.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.utils.StringUtils;

import butterknife.BindView;

/**
 * Created by isanwenyu@163.com on 2017/11/22.
 */
public class InputDialogFragment extends BaseConfirmDialog {

    public static final String TAG = "InputDialogFragment";
    private static final String CONFIRM_INPUT_MESSAGE = "pwd_message";
    private static final String CONFIRM_INPUT_HINT = "input_hint";
    @BindView(R.id.et_confirm_input)
    EditText etConfirmPwd;
    @BindView(R.id.tv_dialog_msg)
    TextView tvDialogMsg;
    private OnConfirmInputListener mOnConfirmInputListener;
    private String mInputMsg;
    private String mInputHint;

    public static InputDialogFragment newInstance(String title, String inputHint, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(CONFIRM_INPUT_HINT, inputHint);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InputDialogFragment newInstance(String title, String inputHint, String msg, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(CONFIRM_INPUT_MESSAGE, msg);
        args.putString(CONFIRM_INPUT_HINT, inputHint);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBeforeSetContentLayout() {

        mInputMsg = getArguments().getString(CONFIRM_INPUT_MESSAGE);
        mInputHint = getArguments().getString(CONFIRM_INPUT_HINT);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        setConfirmDialogClickListener(new ConfirmDialogClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link BaseConfirmDialog#DIALOG_BTN_POSITIVE}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAutoDismiss(true);
                if (which == BaseConfirmDialog.DIALOG_BTN_POSITIVE) {
                    if (StringUtils.isNotEmpty(getConfirmInputText())) {
                        if (mOnConfirmInputListener != null) {
                            mOnConfirmInputListener.onPwdCnfirmed(getConfirmInputText());
                        }
                    } else {
                        setAutoDismiss(false);
                        showMessage(mInputHint);
                        return;
                    }

                } else {
                    // 取消
                    if (mOnConfirmInputListener != null) {
                        mOnConfirmInputListener.onDialogCanceled();
                    }

                }
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (StringUtils.isNotEmpty(mInputMsg)) {
            tvDialogMsg.setVisibility(View.VISIBLE);
            tvDialogMsg.setText(mInputMsg);
        }
        if (StringUtils.isNotEmpty(mInputHint)) {
            etConfirmPwd.setHint(mInputHint);
        }

    }

    @Override
    public void dismissDialog(String tag) {
        etConfirmPwd.setText("");
        super.dismissDialog(tag);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm_input_content;
    }

    public String getConfirmInputText() {
        return etConfirmPwd.getText().toString();
    }

    public InputDialogFragment setOnConfirmInputListener(OnConfirmInputListener onConfirmInputListener) {
        mOnConfirmInputListener = onConfirmInputListener;
        return this;
    }

    public interface OnConfirmInputListener {

        void onPwdCnfirmed(String confirmInput);

        void onDialogCanceled();
    }

}
