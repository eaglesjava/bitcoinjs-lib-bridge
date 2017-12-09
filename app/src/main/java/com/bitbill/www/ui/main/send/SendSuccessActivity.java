package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;

import butterknife.BindView;

public class SendSuccessActivity extends BaseToolbarActivity {

    @BindView(R.id.tv_hint_title)
    TextView tvHintTitle;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.tv_send_amount)
    TextView tvSendAmount;
    @BindView(R.id.tv_hint_content)
    TextView tvHintContent;
    @BindView(R.id.btn_create_contact)
    Button btnCreateContact;

    public static void start(Context context) {
        Intent starter = new Intent(context, SendSuccessActivity.class);
        context.startActivity(starter);
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

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
        return R.layout.activity_send_success;
    }
}
