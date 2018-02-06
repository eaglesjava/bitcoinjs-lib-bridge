package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.dialog.MessageConfirmDialog;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectWalletActivity extends BaseToolbarActivity {
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private CommonAdapter<Wallet> mAdapter;
    private List<Wallet> mWalletList;
    private int mSelectedPos = -1;
    private Wallet mSelectedWallet;
    private String mSendAddress;
    private String mSendAmount;
    private boolean isSendAll;
    private Contact mSendContact;

    public static void start(Context context, String address, String sendAmount, boolean isSendAll, Contact sendContact) {
        Intent starter = new Intent(context, SelectWalletActivity.class);
        starter.putExtra(AppConstants.EXTRA_SEND_ADDRESS, address);
        starter.putExtra(AppConstants.EXTRA_SEND_AMOUNT, sendAmount);
        starter.putExtra(AppConstants.EXTRA_IS_SEND_ALL, isSendAll);
        starter.putExtra(AppConstants.EXTRA_SEND_CONTACT, sendContact);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {

        mSendAddress = getIntent().getStringExtra(AppConstants.EXTRA_SEND_ADDRESS);
        mSendAmount = getIntent().getStringExtra(AppConstants.EXTRA_SEND_AMOUNT);
        isSendAll = getIntent().getBooleanExtra(AppConstants.EXTRA_IS_SEND_ALL, false);
        mSendContact = (Contact) getIntent().getSerializableExtra(AppConstants.EXTRA_SEND_CONTACT);
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
        setTitle(R.string.title_activity_select_wallet);
        if (mWalletList == null) {
            mWalletList = new ArrayList<>();
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        mAdapter = new CommonAdapter<Wallet>(SelectWalletActivity.this, R.layout.item_wallet_select_corner, mWalletList) {

            @Override
            protected void convert(ViewHolder holder, Wallet wallet, final int position) {

                holder.setText(R.id.tv_wallet_name, StringUtils.cutWalletName(wallet.getName()));
                holder.setText(R.id.tv_wallet_amount, StringUtils.satoshi2btc(wallet.getBalance()) + " " + AppConstants.BTC_COIN_TYPE);
                holder.setText(R.id.tv_wallet_label, String.valueOf(wallet.getName().charAt(0)));

                holder.setChecked(R.id.rb_selector, wallet.isSelected());
                if (wallet.getBalance() > StringUtils.btc2Satoshi(mSendAmount)) {
                    //余额可用
                    holder.setVisible(R.id.rb_selector, true);
                    holder.itemView.setAlpha(1.0f);
                } else {
                    holder.setVisible(R.id.rb_selector, false);
                    holder.itemView.setAlpha(0.3f);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //实现单选方法三： RecyclerView另一种定向刷新方法：不会有白光一闪动画 也不会重复onBindVIewHolder
                        //如果勾选的不是已经勾选状态的Item
                        if (mSelectedPos != position && mSelectedPos != -1) {
                            //先取消上个item的勾选状态
                            ViewHolder commonHolder = ((ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mSelectedPos));
                            if (commonHolder != null) {//还在屏幕里
                                commonHolder.setChecked(R.id.rb_selector, false);
                            } else {
                                //add by 2016 11 22 for 一些极端情况，holder被缓存在Recycler的cacheView里，
                                //此时拿不到ViewHolder，但是也不会回调onBindViewHolder方法。所以add一个异常处理
                                notifyItemChanged(mSelectedPos);
                            }
                            mWalletList.get(mSelectedPos).setSelected(false);//不管在不在屏幕里 都需要改变数据
                            //设置新Item的勾选状态
                            mSelectedPos = position;
                            mWalletList.get(mSelectedPos).setSelected(true);
                            holder.setChecked(R.id.rb_selector, wallet.isSelected());
                            mSelectedWallet = wallet;
                        }

                    }
                });

            }
        };
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        mWalletList.clear();
        mWalletList.addAll(BitbillApp.get().getWallets());
        // 选择默认的钱包对象作为选中的
        mSelectedWallet = BitbillApp.get().getDefaultWallet();
        if (mSelectedWallet != null) {
            //重置单选select对象
            for (Wallet wallet : mWalletList) {
                if (wallet.equals(mSelectedWallet)) {
                    wallet.setSelected(true);
                } else {
                    wallet.setSelected(false);
                }
            }
            mSelectedWallet.setSelected(true);
        }

        //  通过钱包对象设置选择后的当前位置
        mSelectedPos = mWalletList.indexOf(getSelectedWallet());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_wallet;
    }


    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        if (!validSelectedWallet()) {
            return;
        }
        if (!isValidBtcBalance()) {
            MessageConfirmDialog.newInstance(getString(R.string.msg_balance_not_enough), true).show(getSupportFragmentManager(), MessageConfirmDialog.TAG);
            return;
        }
        //跳转到确认发送界面
        SendConfirmActivity.start(SelectWalletActivity.this, mSendAddress, isSendAll ? StringUtils.satoshi2btc(mSelectedWallet.getBalance()) : mSendAmount, isSendAll, mSelectedWallet, mSendContact);
    }

    private boolean validSelectedWallet() {
        if (mSelectedWallet == null) {
            showMessage(R.string.msg_please_select_wallet);
            return false;

        }
        return true;
    }

    public Wallet getSelectedWallet() {
        return mSelectedWallet;
    }

    public boolean isValidBtcBalance() {

        if (isSendAll) {
            if (getSelectedWallet().getBalance() > 0) return true;
        } else if (mSendAmount != null) {
            if (getSelectedWallet().getBalance() > StringUtils.btc2Satoshi(mSendAmount)) {
                return true;
            }
        }
        return false;
    }
}
