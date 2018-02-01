package com.bitbill.www.ui.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.bitbill.www.R;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.widget.EditTextWapper;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by isanwenyu@163.com on 2018/1/31.
 */
public class FeebackActivity extends BaseToolbarActivity<FeebackMvpPresenter> implements FeebackMvpView {
    @Inject
    FeebackMvpPresenter<AppModel, FeebackMvpView> mFeebackMvpPresenter;
    @BindView(R.id.et_feeback_content)
    EditText etFeebackContent;
    @BindView(R.id.etw_feeback_contact)
    EditTextWapper etwFeebackContact;

    public static void start(Context context) {
        Intent starter = new Intent(context, FeebackActivity.class);
        context.startActivity(starter);
    }

    @Override
    public FeebackMvpPresenter getMvpPresenter() {
        return mFeebackMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
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
        return R.layout.activity_fee_back;
    }

    @Override
    public String getContent() {
        return etFeebackContent.getText().toString();
    }

    @Override
    public String getContact() {
        return etwFeebackContact.getText();
    }

    @Override
    public void sendFeebackSuccess() {
        showMessage(R.string.success_send_feeback);
        finish();
    }

    @Override
    public void sendFeebackFail() {

        showMessage(R.string.fail_send_feeback);
    }

    @Override
    public void requireContent() {
        showMessage(R.string.content_not_empty);
    }

    @Override
    public void tooMuchWords() {
        showMessage(R.string.fail_too_much_words);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeback_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feeback_send) {
            //发送反馈
            getMvpPresenter().sendFeeback();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
