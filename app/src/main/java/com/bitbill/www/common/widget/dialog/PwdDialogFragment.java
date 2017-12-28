package com.bitbill.www.common.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import butterknife.BindView;

/**
 * Created by isanwenyu@163.com on 2017/11/22.
 */
public class PwdDialogFragment extends BaseConfirmDialog {

    public static final String TAG = "InputDialogFragment";
    private static final String CONFIRM_WALLET = "confirm_wallet";
    private static final String PWD_MESSAGE = "pwd_message";
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.tv_dialog_msg)
    TextView tvDialogMsg;
    private Wallet mWallet;
    private OnPwdValidatedListener mOnPwdValidatedListener;
    private String mPwdMsg;

    public static PwdDialogFragment newInstance(String title, Wallet wallet, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putSerializable(CONFIRM_WALLET, wallet);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        PwdDialogFragment fragment = new PwdDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PwdDialogFragment newInstance(String title, String msg, Wallet wallet, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putString(PWD_MESSAGE, msg);
        args.putSerializable(CONFIRM_WALLET, wallet);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        PwdDialogFragment fragment = new PwdDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBeforeSetContentLayout() {
        mWallet = (Wallet) getArguments().getSerializable(CONFIRM_WALLET);
        mPwdMsg = getArguments().getString(PWD_MESSAGE);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        setConfirmDialogClickListener(new BaseConfirmDialog.ConfirmDialogClickListener() {
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
                    if (StringUtils.checkUserPwd(getConfirmPwd(), mWallet)) {
                        // 确定密码验证正确
                        if (mOnPwdValidatedListener != null) {
                            mOnPwdValidatedListener.onPwdCnfirmed(getConfirmPwd());
                        }
                    } else {
                        setAutoDismiss(false);
                        showMessage("请输入正确的密码");
                        return;
                    }

                } else {
                    // 取消
                    if (mOnPwdValidatedListener != null) {
                        mOnPwdValidatedListener.onDialogCanceled();
                    }

                }
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (StringUtils.isNotEmpty(mPwdMsg)) {
            tvDialogMsg.setVisibility(View.VISIBLE);
            tvDialogMsg.setText(mPwdMsg);
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
        return R.layout.dialog_confirm_pwd_content;
    }

    public String getConfirmPwd() {
        return etConfirmPwd.getText().toString();
    }

    public PwdDialogFragment setOnPwdValidatedListener(OnPwdValidatedListener onPwdValidatedListener) {
        mOnPwdValidatedListener = onPwdValidatedListener;
        return this;
    }

    public interface OnPwdValidatedListener {

        void onPwdCnfirmed(String confirmPwd);

        void onDialogCanceled();
    }

}
