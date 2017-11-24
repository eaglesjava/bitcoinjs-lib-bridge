package com.bitbill.www.common.base.view.dialog;

import android.os.Bundle;

import com.bitbill.www.R;

/**
 * Created by isanwenyu@163.com on 2017/11/24.
 */
public class SupportCoinDialog extends BaseConfirmDialog {

    public static final String TAG = "SupportCoinDialog";

    public static SupportCoinDialog newInstance(String title, boolean isOnlyPositiveBtn, String positiveText) {

        Bundle args = new Bundle();

        args.putString(CONFIRM_TITLE, title);
        args.putBoolean(CONFIRM_ONLY_POSITIVE_BTN, isOnlyPositiveBtn);
        args.putString(CONFIRM_POSITIVE_BTN_TEXT, positiveText);
        SupportCoinDialog fragment = new SupportCoinDialog();
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
        return R.layout.dialog_confirm_support_coin;
    }
}
