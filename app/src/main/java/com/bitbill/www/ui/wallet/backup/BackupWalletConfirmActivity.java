package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.eventbus.WalletUpdateEvent;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class BackupWalletConfirmActivity extends BaseToolbarActivity<BackupWalletConfirmMvpPresenter> implements BackupWalletConfirmMvpView {

    @BindView(R.id.fl_mnemonic_confirm)
    TagFlowLayout mFlMnemonicConfirm;
    @BindView(R.id.fl_mnemonic)
    TagFlowLayout mFlMnemonic;
    @BindView(R.id.tv_hint_click)
    TextView tvHintClick;
    @Inject
    BackupWalletConfirmMvpPresenter<WalletModel, BackupWalletConfirmMvpView> mBackupWalletConfrimMvpPresenter;

    private String mMnemonic;
    private String[] mMnemonicArray;
    private List<String> mMnemonicList;
    private List<String> mMnemonicConfirmList;
    private TagAdapter<String> mMnemonicAdapter;
    private TagAdapter<String> mMnemonicConfrimAdapter;
    private Wallet mWallet;
    private boolean isFromSetting;

    public static void start(Context context, String mnemonic, Wallet wallet, boolean isFromSetting) {
        Intent intent = new Intent(context, BackupWalletConfirmActivity.class);
        intent.putExtra(AppConstants.EXTRA_MNEMONIC, mnemonic);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        intent.putExtra(AppConstants.EXTRA_IS_FROM_SETTING, isFromSetting);
        context.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);

        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        mMnemonic = getIntent().getStringExtra(AppConstants.EXTRA_MNEMONIC);
        isFromSetting = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_FROM_SETTING, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cancel_mnemonic, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel_mnemonic) {
            //清空助记词
            mMnemonicConfirmList.clear();
            mMnemonicConfrimAdapter.notifyDataChanged();
            //重置提示布局
            tvHintClick.setVisibility(mMnemonicConfirmList.size() > 0 ? View.GONE : View.VISIBLE);
            for (int i = 0; i < mFlMnemonic.getChildCount(); i++) {
                mMnemonicAdapter.setSelectedList(new HashSet<>());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public BackupWalletConfirmMvpPresenter getMvpPresenter() {
        return mBackupWalletConfrimMvpPresenter;
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
        mMnemonicArray = mMnemonic.split(" ");
        //直接赋值 对mMnemonicList排序mMnemonicArray也会受影响
        mMnemonicList = new ArrayList<>();
        mMnemonicList.addAll(Arrays.asList(mMnemonicArray));

        // 对助记词进行乱序排序
        Collections.shuffle(mMnemonicList);

        // 助记词确定框
        mMnemonicConfirmList = new ArrayList<>();
        mMnemonicConfrimAdapter = new TagAdapter<String>(mMnemonicConfirmList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView mnemonicView = (TextView) mInflater.inflate(R.layout.item_mnemonic_confirm_text, null);
                mnemonicView.setText(s);
                return mnemonicView;
            }
        };
        mFlMnemonicConfirm.setAdapter(mMnemonicConfrimAdapter);
        //助记词点击框
        mMnemonicAdapter = new TagAdapter<String>(mMnemonicList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                CheckedTextView mnemonicView = (CheckedTextView) mInflater.inflate(R.layout.item_mnemonic_text, null);
                mnemonicView.setText(s);
                return mnemonicView;
            }
        };
        mFlMnemonic.setAdapter(mMnemonicAdapter);
        mFlMnemonic.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String clickItem = mMnemonicList.get(position);
                if (mFlMnemonic.getSelectedList().contains(position)) {
                    mMnemonicConfirmList.add(clickItem);
                } else {
                    mMnemonicConfirmList.remove(clickItem);
                }
                mMnemonicConfrimAdapter.notifyDataChanged();
                tvHintClick.setVisibility(mMnemonicConfirmList.size() > 0 ? View.GONE : View.VISIBLE);
                return true;
            }
        });
        //设置确定对话框布局高度
        mFlMnemonicConfirm.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= 16) {
                            mFlMnemonicConfirm.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            mFlMnemonicConfirm.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        mFlMnemonicConfirm.getLayoutParams().height = mFlMnemonic.getMeasuredHeight();
                        mFlMnemonicConfirm.requestLayout();
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_backup_wallet_confirm;
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        mBackupWalletConfrimMvpPresenter.checkBackup();
    }

    public String[] getMnemonicArray() {
        return mMnemonicArray;
    }

    public List<String> getMnemonicConfirmList() {
        return mMnemonicConfirmList;
    }

    @Override
    public Wallet getWallet() {
        return mWallet;
    }


    @Override
    public void backupSuccess() {

        //跳转到备份成功界面
        BackupWalletSuccessActivity.start(BackupWalletConfirmActivity.this, isFromSetting);
        EventBus.getDefault().postSticky(new WalletUpdateEvent(getWallet()));
        finish();
    }

    @Override
    public void backupFail() {
        // 弹出不匹配提示
        MessageConfirmDialog.newInstance(getString(R.string.title_dialog_backup_fail), getString(R.string.msg_dailog_check_mnemonic), false)
                .show(getSupportFragmentManager(), MessageConfirmDialog.TAG);

    }

}
