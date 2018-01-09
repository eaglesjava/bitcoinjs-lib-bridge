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
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.bitbill.www.model.wallet.network.entity.TxElement;
import com.bitbill.www.model.wallet.network.entity.TxItem;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferDetailFragment extends BaseListFragment<TransferItem, MvpPresenter> {

    private TxItem mTxItem;
    private HeaderViewHolder mHeaderViewHolder;
    private FooterViewHolder mFooterViewHolder;

    public static TransferDetailFragment newInstance(TxItem txItem) {

        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_TX_ITEM, txItem);
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
        mTxItem = ((TxItem) getArguments().getSerializable(AppConstants.ARG_TX_ITEM));
        if (mTxItem == null) {
            showMessage(R.string.fail_load_transfer_details);
            return;
        }
        //构造列表数据
        mDatas.clear();
        mDatas.add(new TransferHashItem().setHash(mTxItem.getTxHash()).setTitle(getString(R.string.title_tx_hash)));
        for (TxElement.InputsBean inputsBean : mTxItem.getInputs()) {
            mDatas.add(new TransferSendItem().setAddress(inputsBean.getAddress()).setAmount(inputsBean.getValue()).setTitle(getString(R.string.title_tx_send_address)));
        }
        for (TxElement.OutputsBean outputsBean : mTxItem.getOutputs()) {
            mDatas.add(new TransferReceiveItem().setAddress(outputsBean.getAddress()).setAmount(outputsBean.getValue()).setTitle(getString(R.string.title_tx_receive_address)));
        }
        mDatas.add(new TransferRemarkItem().setRemark(mTxItem.getRemark()).setTitle(getString(R.string.title_tx_remark)));
        mDatas.add(new TransferDateItem().setDate(StringUtils.formatDate(mTxItem.getCreatedTime())).setTitle(getString(R.string.title_tx_date)));
        mAdapter.notifyDataSetChanged();

        if (mHeaderViewHolder != null) {

            String inOutString = mTxItem.getInOut() == TxItem.InOut.TRANSFER ? "" : (mTxItem.getInOut() == TxItem.InOut.IN ? "+" : "-");
            mHeaderViewHolder.mTvTransferAmount.setText(inOutString + StringUtils.satoshi2btc(mTxItem.getSumAmount()) + " btc");
            StringUtils.setAmountTypeface(getBaseActivity(), mHeaderViewHolder.mTvTransferAmount);
            if (mTxItem.getHeight() == -1) {
                mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_unconfirm);
                mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_unconfirm);
            } else {
                switch (mTxItem.getInOut()) {
                    case TRANSFER:
                        mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_item_transfer);
                        mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_self);
                        break;
                    case IN:
                        mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_success);
                        mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_in);
                        break;
                    case OUT:
                        mHeaderViewHolder.mIvTransferStatus.setImageResource(R.drawable.ic_transfer_success);
                        mHeaderViewHolder.mTvTransferStatus.setText(R.string.status_transfer_out);
                        break;
                }
            }
        }
        if (mFooterViewHolder != null) {
            mFooterViewHolder.mTvViewTx.setOnClickListener(v -> UIHelper.openBrower(getBaseActivity(), AppConstants.TX_BLOCK_CHAIN_PREFIX + mTxItem.getTxHash()));
        }
    }

    @Override
    protected void onListItemClick(TransferItem transferItem, int position) {
        if (transferItem instanceof TransferHashItem) {

            TransferHashItem hashItem = (TransferHashItem) transferItem;
            StringUtils.copy(hashItem.getHash(), getBaseActivity());
            showMessage(R.string.copy_tx_hash);

        } else if (transferItem instanceof TransferSendItem) {

            TransferSendItem sendItem = (TransferSendItem) transferItem;
            StringUtils.copy(sendItem.getAddress(), getBaseActivity());
            showMessage(R.string.copy_send_address);

        } else if (transferItem instanceof TransferReceiveItem) {

            TransferReceiveItem receiveItem = (TransferReceiveItem) transferItem;
            StringUtils.copy(receiveItem.getAddress(), getBaseActivity());
            showMessage(R.string.copy_receive_address);

        } else if (transferItem instanceof TransferRemarkItem) {

            TransferRemarkItem remarkItem = (TransferRemarkItem) transferItem;
            StringUtils.copy(remarkItem.getRemark(), getBaseActivity());
            showMessage(R.string.copy_remark);

        } else if (transferItem instanceof TransferDateItem) {

            TransferDateItem dateItem = (TransferDateItem) transferItem;
            StringUtils.copy(dateItem.getDate(), getBaseActivity());
            showMessage(R.string.copy_tx_date);
        }
    }

    @Override
    protected void itemConvert(ViewHolder holder, TransferItem transferItem, int position) {
        //跟前一个title不一样 显示title布局
        if (position == 1 || (StringUtils.isNotEmpty(transferItem.getTitle()) && !transferItem.getTitle().equals(mDatas.get(position - 2).getTitle()))) {
            holder.setVisible(R.id.tv_tx_title, true);
        } else {
            holder.setVisible(R.id.tv_tx_title, false);
        }

        if (transferItem instanceof TransferHashItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_hash));
            TransferHashItem hashItem = (TransferHashItem) transferItem;
            holder.setText(R.id.tv_tx_left, hashItem.getHash());
            holder.setVisible(R.id.tv_tx_right, false);

        } else if (transferItem instanceof TransferSendItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_send_address));
            TransferSendItem sendItem = (TransferSendItem) transferItem;
            holder.setText(R.id.tv_tx_left, sendItem.getAddress());
            holder.setText(R.id.tv_tx_right, StringUtils.satoshi2btc(sendItem.getAmount()) + " BTC");
            holder.setVisible(R.id.tv_tx_right, true);

        } else if (transferItem instanceof TransferReceiveItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_receive_address));
            TransferReceiveItem receiveItem = (TransferReceiveItem) transferItem;
            holder.setText(R.id.tv_tx_left, receiveItem.getAddress());
            holder.setText(R.id.tv_tx_right, StringUtils.satoshi2btc(receiveItem.getAmount()) + " BTC");
            holder.setVisible(R.id.tv_tx_right, true);

        } else if (transferItem instanceof TransferRemarkItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_remark));
            TransferRemarkItem remarkItem = (TransferRemarkItem) transferItem;
            holder.setText(R.id.tv_tx_left, remarkItem.getRemark());
            holder.setVisible(R.id.tv_tx_right, false);

        } else if (transferItem instanceof TransferDateItem) {
            holder.setText(R.id.tv_tx_title, getString(R.string.title_tx_date));
            TransferDateItem dateItem = (TransferDateItem) transferItem;
            holder.setText(R.id.tv_tx_left, dateItem.getDate());
            holder.setVisible(R.id.tv_tx_right, false);

        }
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
