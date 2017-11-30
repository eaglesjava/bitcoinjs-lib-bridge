package com.bitbill.www.ui.wallet.backup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.dialog.MessageConfirmDialog;
import com.bitbill.www.common.base.view.widget.FocusedCheckedTextView;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupWalletConfirmActivity extends BaseToolbarActivity<BackupWalletConfirmMvpPresenter> implements BackupWalletConfirmMvpView {

    @BindView(R.id.gv_mnemonic_confirm)
    GridView gvMnemonicConfirm;
    @BindView(R.id.gv_mnemonic)
    GridView gvMnemonic;
    @BindView(R.id.tv_hint_click)
    TextView tvHintClick;
    @Inject
    BackupWalletConfirmMvpPresenter<WalletModel, BackupWalletConfirmMvpView> mBackupWalletConfrimMvpPresenter;

    private String mMnemonic;
    private String[] mMnemonicArray;
    private List<String> mMnemonicList;
    private List<String> mMnemonicConfirmList;
    private GridViewAdapter mMnemonicAdapter;
    private GridViewAdapter mMnemonicConfrimAdapter;
    private Wallet mWallet;

    public static void start(Context context, String mnemonic, Wallet wallet) {
        Intent intent = new Intent(context, BackupWalletConfirmActivity.class);
        intent.putExtra(AppConstants.EXTRA_MNEMONIC, mnemonic);
        intent.putExtra(AppConstants.EXTRA_WALLET, wallet);
        context.startActivity(intent);
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
        mWallet = (Wallet) getIntent().getSerializableExtra(AppConstants.EXTRA_WALLET);
        mMnemonic = getIntent().getStringExtra(AppConstants.EXTRA_MNEMONIC);
        mMnemonicArray = mMnemonic.split(" ");
        //直接赋值 对mMnemonicList排序mMnemonicArray也会受影响
        mMnemonicList = new ArrayList<>();
        mMnemonicList.addAll(Arrays.asList(mMnemonicArray));

        // 对助记词进行乱序排序
        Collections.sort(mMnemonicList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.hashCode() - o2.hashCode();
            }
        });

        // 助记词确定框
        mMnemonicConfirmList = new ArrayList<>();
        mMnemonicConfrimAdapter = new GridViewAdapter(this, mMnemonicConfirmList);
        gvMnemonicConfirm.setAdapter(mMnemonicConfrimAdapter);
        //助记词点击框
        mMnemonicAdapter = new GridViewAdapter(this, mMnemonicList);
        gvMnemonic.setAdapter(mMnemonicAdapter);
        gvMnemonic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //检查重复
                String item = mMnemonicList.get(position);
                if (mMnemonicConfirmList.contains(item)) {
                    mMnemonicConfirmList.remove(item);
                } else if (mMnemonicConfirmList.size() < mMnemonicList.size()) {
                    mMnemonicConfirmList.add(item);
                }
                mMnemonicConfrimAdapter.notifyDataSetChanged();
                tvHintClick.setVisibility(mMnemonicConfirmList.size() > 0 ? View.GONE : View.VISIBLE);

            }
        });
        //设置确定对话框布局高度
        gvMnemonicConfirm.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= 16) {
                            gvMnemonicConfirm.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            gvMnemonicConfirm.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        gvMnemonicConfirm.getLayoutParams().height = gvMnemonic.getMeasuredHeight();
                        gvMnemonicConfirm.requestLayout();
                    }
                });
//        gvMnemonic.setNumColumns(mMnemonicArray.length / 2);
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
        BackupWalletSuccessActivity.start(BackupWalletConfirmActivity.this);
        finish();
    }

    @Override
    public void backupFail() {
        // 弹出不匹配提示
        MessageConfirmDialog.newInstance(getString(R.string.title_dialog_backup_fail), getString(R.string.msg_dailog_check_mnemonic), false)
                .show(getSupportFragmentManager(), MessageConfirmDialog.TAG);

    }

    public static class GridViewAdapter extends BaseAdapter {
        private List<String> mList;
        private LayoutInflater mInflater;

        public GridViewAdapter(Context context, List<String> list) {
            mList = list;
            mInflater = LayoutInflater.from(context);
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return mList.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_mnemonic_text, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvMnemonic.setText(getItem(position));
            return convertView;
        }

        static class ViewHolder {
            @BindView(R.id.tv_mnemonic)
            FocusedCheckedTextView tvMnemonic;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
