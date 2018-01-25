package com.bitbill.www.ui.wallet.info.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.model.entity.TitleItem;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.bitbill.www.model.transaction.db.entity.Input;
import com.bitbill.www.model.transaction.db.entity.Output;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferDetailFragment extends BaseListFragment<TitleItem, MvpPresenter> {

    private TxRecord mTxRecord;
    private HeaderViewHolder mHeaderViewHolder;
    private FooterViewHolder mFooterViewHolder;

    public static TransferDetailFragment newInstance(TxRecord txRecord) {

        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_TX_ITEM, txRecord);
        TransferDetailFragment fragment = new TransferDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        View header = LayoutInflater.from(getBaseActivity()).inflate(R.layout.header_transfer_details, mRecyclerView, false);
        mHeaderViewHolder = new HeaderViewHolder(header);
        View footer = LayoutInflater.from(getBaseActivity()).inflate(R.layout.footer_transfer_details, mRecyclerView, false);
        mFooterViewHolder = new FooterViewHolder(footer);
        headerAndFooterWrapper.addHeaderView(header);
        headerAndFooterWrapper.addFootView(footer);
        super.setAdapter(headerAndFooterWrapper);
    }

    @Override
    public void initData() {
        mTxRecord = ((TxRecord) getArguments().getSerializable(AppConstants.ARG_TX_ITEM));
        if (mTxRecord == null) {
            showMessage(R.string.fail_load_transfer_details);
            return;
        }
        mTxRecord.__setDaoSession(getApp().getDaoSession());
        //构造列表数据
        mDatas.clear();
        mDatas.add(new TransferHashItem().setHash(mTxRecord.getTxHash()).setTitle(getString(R.string.title_tx_hash)));
        try {
            for (Input inputsBean : mTxRecord.getInputs()) {
                mDatas.add(new TransferSendItem().setAddress(inputsBean.getAddress()).setAmount(inputsBean.getValue()).setTitle(getString(R.string.title_tx_send_address)));
            }
            for (Output outputsBean : mTxRecord.getOutputs()) {
                mDatas.add(new TransferReceiveItem().setAddress(outputsBean.getAddress()).setAmount(outputsBean.getValue()).setTitle(getString(R.string.title_tx_receive_address)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long height = mTxRecord.getHeight();
        if (height != -1) {
            //非待确认状态
            mDatas.add(new TransferConfirmItem(height).setTitle(getString(R.string.title_tx_confirm)));
        }
        mDatas.add(new TransferRemarkItem().setRemark(mTxRecord.getRemark()).setTitle(getString(R.string.title_tx_remark)));
        mDatas.add(new TransferDateItem().setDate(StringUtils.formatDateTime(mTxRecord.getCreatedTime())).setTitle(getString(R.string.title_tx_date)));
        mAdapter.notifyDataSetChanged();

        if (mHeaderViewHolder != null) {

            String inOutString = mTxRecord.getInOut() == TxRecord.InOut.TRANSFER ? "" : (mTxRecord.getInOut() == TxRecord.InOut.IN ? "+" : "-");
            mHeaderViewHolder.mTvTransferAmount.setText(inOutString + StringUtils.satoshi2btc(mTxRecord.getSumAmount()) + " btc");
            StringUtils.setAmountTypeface(getBaseActivity(), mHeaderViewHolder.mTvTransferAmount);
            if (mTxRecord.getHeight() == -1) {
                mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_unconfirm);
                mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_unconfirm);
            } else {
                switch (mTxRecord.getInOut()) {
                    case TRANSFER:
                        mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_success);
                        mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_success);
                        break;
                    case IN:
                        mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_success);
                        mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_success);
                        break;
                    case OUT:
                        mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_success);
                        mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_success);
                        break;
                }
            }
        }
        if (mFooterViewHolder != null) {
            mFooterViewHolder.mTvViewTx.setOnClickListener(v -> UIHelper.openBrower(getBaseActivity(), AppConstants.TX_BLOCK_CHAIN_PREFIX + mTxRecord.getTxHash()));
        }
    }

    @Override
    protected void onListItemClick(TitleItem titleItem, int position) {
        if (titleItem instanceof TransferHashItem) {

            TransferHashItem hashItem = (TransferHashItem) titleItem;
            UIHelper.copy(getBaseActivity(), hashItem.getHash());
            showMessage(R.string.copy_tx_hash);

        } else if (titleItem instanceof TransferSendItem) {

            TransferSendItem sendItem = (TransferSendItem) titleItem;
            UIHelper.copy(getBaseActivity(), sendItem.getAddress());
            showMessage(R.string.copy_send_address);

        } else if (titleItem instanceof TransferReceiveItem) {

            TransferReceiveItem receiveItem = (TransferReceiveItem) titleItem;
            UIHelper.copy(getBaseActivity(), receiveItem.getAddress());
            showMessage(R.string.copy_receive_address);

        } else if (titleItem instanceof TransferConfirmItem) {

            TransferConfirmItem confirmItem = (TransferConfirmItem) titleItem;
            UIHelper.copy(getBaseActivity(), getConfirmCount(confirmItem));
            showMessage(R.string.copy_confim_count);

        } else if (titleItem instanceof TransferRemarkItem) {

            TransferRemarkItem remarkItem = (TransferRemarkItem) titleItem;
            UIHelper.copy(getBaseActivity(), remarkItem.getRemark());
            showMessage(R.string.copy_remark);

        } else if (titleItem instanceof TransferDateItem) {

            TransferDateItem dateItem = (TransferDateItem) titleItem;
            UIHelper.copy(getBaseActivity(), dateItem.getDate());
            showMessage(R.string.copy_tx_date);
        }
    }

    @Override
    protected void itemConvert(ViewHolder holder, TitleItem titleItem, int position) {
        //跟前一个title不一样 显示title布局
        if (position == 1 || (StringUtils.isNotEmpty(titleItem.getTitle()) && !titleItem.getTitle().equals(mDatas.get(position - 2).getTitle()))) {
            holder.setVisible(R.id.tv_tx_title, true);
        } else {
            holder.setVisible(R.id.tv_tx_title, false);
        }

        if (titleItem instanceof TransferHashItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_hash));
            TransferHashItem hashItem = (TransferHashItem) titleItem;
            holder.setText(R.id.tv_tx_left, hashItem.getHash());
            holder.setVisible(R.id.tv_tx_right, false);

        } else if (titleItem instanceof TransferSendItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_send_address));
            TransferSendItem sendItem = (TransferSendItem) titleItem;
            holder.setText(R.id.tv_tx_left, sendItem.getAddress());
            holder.setText(R.id.tv_tx_right, StringUtils.satoshi2btc(sendItem.getAmount()) + " BTC");
            holder.setVisible(R.id.tv_tx_right, true);

        } else if (titleItem instanceof TransferReceiveItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_receive_address));
            TransferReceiveItem receiveItem = (TransferReceiveItem) titleItem;
            holder.setText(R.id.tv_tx_left, receiveItem.getAddress());
            holder.setText(R.id.tv_tx_right, StringUtils.satoshi2btc(receiveItem.getAmount()) + " BTC");
            holder.setVisible(R.id.tv_tx_right, true);

        } else if (titleItem instanceof TransferConfirmItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_confirm));
            holder.setText(R.id.tv_tx_left, getConfirmCount((TransferConfirmItem) titleItem));
            holder.setVisible(R.id.tv_tx_right, false);

        } else if (titleItem instanceof TransferRemarkItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_remark));
            TransferRemarkItem remarkItem = (TransferRemarkItem) titleItem;
            holder.setText(R.id.tv_tx_left, remarkItem.getRemark());
            holder.setVisible(R.id.tv_tx_right, false);

        } else if (titleItem instanceof TransferDateItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_date));
            TransferDateItem dateItem = (TransferDateItem) titleItem;
            holder.setText(R.id.tv_tx_left, dateItem.getDate());
            holder.setVisible(R.id.tv_tx_right, false);

        }
    }

    private String getConfirmCount(TransferConfirmItem transferItem) {
        TransferConfirmItem confirmItem = transferItem;
        return String.valueOf(BitbillApp.get().getBlockHeight() - confirmItem.getHeight() + 1);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_transfer_detail;
    }

    @NonNull
    @Override
    public DividerDecoration getDecoration() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_transfer_details;
    }

    static class HeaderViewHolder {

        @BindView(R.id.iv_transfer_status)
        ImageView mIvTransferStatus;
        @BindView(R.id.tv_transfer_status)
        TextView mTvTransferStatus;
        @BindView(R.id.tv_transfer_amount)
        TextView mTvTransferAmount;

        public HeaderViewHolder(View container) {
            ButterKnife.bind(this, container);
        }
    }

    static class FooterViewHolder {
        @BindView(R.id.tv_view_tx)
        TextView mTvViewTx;

        FooterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
