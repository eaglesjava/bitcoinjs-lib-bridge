package com.bitbill.www.common.base.view.widget;

import android.os.Bundle;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseConfirmDialog;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by isanwenyu@163.com on 2017/11/22.
 */
public class PwdDialogFragment extends BaseConfirmDialog {

    public static final String TAG = "InputDialogFragment";
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    Unbinder unbinder;

    public static PwdDialogFragment newInstance(String title, boolean isOnlyPositiveBtn) {

        Bundle args = new Bundle();
        args.putString(CONFIRM_TITLE, title);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        PwdDialogFragment fragment = new PwdDialogFragment();
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

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm_pwd_content;
    }

    public String getConfirmPwd() {
        return etConfirmPwd.getText().toString();
    }

}
