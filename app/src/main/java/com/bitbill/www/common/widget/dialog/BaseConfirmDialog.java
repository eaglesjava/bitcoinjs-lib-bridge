package com.bitbill.www.common.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseViewControl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by isanwenyu@163.com on 2017/11/22.
 */
public abstract class BaseConfirmDialog extends BaseDialog implements BaseViewControl, DialogInterface {

    public static final int DIALOG_BTN_NEGATIVE = R.id.dialog_btn_negative;
    public static final int DIALOG_BTN_POSITIVE = R.id.dialog_btn_positive;
    public static final String CONFIRM_TITLE = "confirm_title";
    public static final String CONFIRM_ONLY_POSITIVE_BTN = "confirm_only_positive_btn";
    public static final String CONFIRM_POSITIVE_BTN_TEXT = "positive_btn_text";
    @BindView(R.id.dialog_title)
    TextView mDialogTitle;
    @BindView(R.id.dialog_container)
    FrameLayout mDialogContainer;
    @BindView(R.id.dialog_btn_negative)
    Button mDialogBtnNegative;
    @BindView(R.id.dialog_btn_positive)
    Button mDialogBtnPositive;
    @BindView(R.id.dialog_btn_container)
    LinearLayout mDialogBtnContainer;
    ConfirmDialogClickListener mConfirmDialogClickListener;
    private LayoutInflater mLayoutInflate;
    private String mTitle;
    private boolean mOnlyPositiveBtn;
    private boolean autoDismiss = true;
    private String mPositiveText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflate = LayoutInflater.from(getContext());
        mTitle = getArguments().getString(CONFIRM_TITLE, null);
        mPositiveText = getArguments().getString(CONFIRM_POSITIVE_BTN_TEXT, null);
        mOnlyPositiveBtn = getArguments().getBoolean(CONFIRM_ONLY_POSITIVE_BTN, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBeforeSetContentLayout();
        View view = mLayoutInflate.inflate(R.layout.dialog_confirm_container, container);
        if (getLayoutId() != 0) {
            ((FrameLayout) view.findViewById(R.id.dialog_container)).addView(mLayoutInflate.inflate(getLayoutId(), null));
        }
        setUnBinder(ButterKnife.bind(this, view));
        init(savedInstanceState);
        //init view
        if (!TextUtils.isEmpty(mTitle)) {
            mDialogTitle.setVisibility(View.VISIBLE);
            mDialogTitle.setText(mTitle);
        }
        if (mOnlyPositiveBtn) {
            mDialogBtnNegative.setVisibility(View.GONE);
            mDialogBtnPositive.setText(R.string.dialog_btn_known);
        }
        if (!TextUtils.isEmpty(mPositiveText)) {
            mDialogBtnPositive.setVisibility(View.VISIBLE);
            mDialogBtnPositive.setText(mPositiveText);
        }
        initView();
        initData();
        return view;
    }

    @OnClick({R.id.dialog_btn_negative, R.id.dialog_btn_positive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_negative:
                if (mConfirmDialogClickListener != null) {
                    mConfirmDialogClickListener.onClick(this, DIALOG_BTN_NEGATIVE);
                }
                autoDismissDialog();
                break;
            case R.id.dialog_btn_positive:
                if (mConfirmDialogClickListener != null) {
                    mConfirmDialogClickListener.onClick(this, DIALOG_BTN_POSITIVE);
                }
                autoDismissDialog();
                break;
        }
    }

    private void autoDismissDialog() {
        if (autoDismiss) {
            dismissDialog(this.getClass().getSimpleName());
        }
    }

    @Override
    public void dismissDialog(String tag) {
        super.dismissDialog(tag);
    }

    public BaseConfirmDialog setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return this;
    }

    @Override
    public void cancel() {
        dismissDialog(this.getClass().getSimpleName());
    }

    public BaseConfirmDialog setConfirmDialogClickListener(ConfirmDialogClickListener confirmDialogClickListener) {
        this.mConfirmDialogClickListener = confirmDialogClickListener;
        return this;
    }

    public interface ConfirmDialogClickListener extends DialogInterface.OnClickListener {

    }
}
