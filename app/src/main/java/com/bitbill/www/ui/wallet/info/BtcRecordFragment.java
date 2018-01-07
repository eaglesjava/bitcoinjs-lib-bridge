package com.bitbill.www.ui.wallet.info;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseLazyListFragment;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.TxElement;
import com.bitbill.www.model.wallet.network.entity.TxItem;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTransactionRecordItemClickListener}
 * interface.
 */
public class BtcRecordFragment extends BaseLazyListFragment<TxItem, BtcRecordMvpPresenter> implements BtcRecordMvpView, ParseTxInfoMvpView {

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amount_label)
    TextView tvAmountLabel;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcCny;
    @BindView(R.id.tv_btc_unconfirm)
    TextView tvBtcUnconfirm;
    @Inject
    BtcRecordMvpPresenter<WalletModel, BtcRecordMvpView> mBtcRecordMvpPresenter;
    @Inject
    ParseTxInfoMvpPresenter<AddressModel, ParseTxInfoMvpView> mViewParseTxInfoMvpPresenter;
    private OnTransactionRecordItemClickListener mListener;
    private Wallet mWalelt;
    private List<TxElement> mTxElementList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BtcRecordFragment() {
    }

    public static BtcRecordFragment newInstance(Wallet wallet) {
        BtcRecordFragment fragment = new BtcRecordFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_WALLET, wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTransactionRecordItemClickListener) {
            mListener = (OnTransactionRecordItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTransactionRecordItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public BtcRecordMvpPresenter getMvpPresenter() {
        return mBtcRecordMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
        addPresenter(mViewParseTxInfoMvpPresenter);
    }

    @Override
    protected void onListItemClick(TxItem txItem, int position) {
        if (mListener != null) {
            mListener.OnTransactionRecordItemClick(txItem);
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_btc_record;
    }

    @Override
    protected void itemConvert(ViewHolder holder, TxItem txItem, int position) {
        holder.setText(R.id.tv_address, txItem.getInOut() == TxItem.InOut.OUT ? txItem.getGatherAddressIn() : txItem.getGatherAddressOut());
        String inOutString = txItem.getInOut() == TxItem.InOut.TRANSFER ? "" : (txItem.getInOut() == TxItem.InOut.IN ? "+" : "-");
        holder.setText(R.id.tv_amount, inOutString + StringUtils.satoshi2btc(txItem.getSumAmount()) + " btc");

        holder.setText(R.id.tv_date, txItem.getCreatedTime());
        if (txItem.getHeight() == -1) {
            holder.setImageResource(R.id.iv_status, R.drawable.ic_item_unconfirm);
            holder.setAlpha(R.id.tv_confirm_count, 0.6f);
            holder.setText(R.id.tv_confirm_count, "未确认");
        } else {
            long confirmCount = BitbillApp.get().getBlockHeight() - txItem.getHeight() + 1;
            holder.setAlpha(R.id.tv_confirm_count, 1.0f);
            holder.setText(R.id.tv_confirm_count, confirmCount + "确认");
            switch (txItem.getInOut()) {
                case TRANSFER:
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_transfer);
                    break;
                case IN:
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_receive);
                    break;
                case OUT:
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_send);
                    break;
            }
        }
    }

    @Override
    public int getRefreshSchemeColor() {
        return R.color.blue;
    }

    @Override
    public void onLayoutRefresh() {
        lazyData();
    }

    @Override
    public void onBeforeSetContentLayout() {
        mWalelt = (Wallet) getArguments().getSerializable(AppConstants.ARG_WALLET);


    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        super.initView();
        StringUtils.setAmountTypeface(getBaseActivity(), tvAmount);
    }

    @Override
    public void initData() {


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_record_list;
    }

    @Override
    public void lazyData() {


        if (mWalelt != null) {
            tvAmount.setText(StringUtils.satoshi2btc(mWalelt.getBalance()));
            tvBtcUnconfirm.setText(String.format(getString(R.string.text_btc_unconfirm), StringUtils.satoshi2btc(mWalelt.getUnconfirm())));

        }
        tvBtcCny.setText(BitbillApp.get().getBtcValue(StringUtils.satoshi2btc(mWalelt.getBalance())));
        getMvpPresenter().getTxRecord();
    }

    @Override
    public Wallet getWallet() {
        return mWalelt;
    }

    @Override
    public void getWalletFail() {
        showMessage(R.string.error_get_wallet_info_fail);
    }

    @Override
    public void getTxRecordSuccess(List<TxElement> list) {
        if (!StringUtils.isEmpty(list)) {
            mTxElementList = list;
            mViewParseTxInfoMvpPresenter.parseTxInfo();
        }
    }

    @Override
    public void getTxRecordFail() {
        showMessage(R.string.fail_get_tx_record);
    }

    @Override
    public List<TxElement> getTxInfoList() {
        return mTxElementList;
    }

    @Override
    public void requireTxInfoList() {

    }

    @Override
    public void getTxInfoListFail() {

    }

    @Override
    public void parsedTxItemList(List<TxItem> txItems) {
        setDatas(txItems);
    }

    @Override
    public void parsedTxItemListFail() {
        showMessage(R.string.fail_parse_tx_item_list);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragme.nt to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTransactionRecordItemClickListener {
        // TODO: Update argument type and name
        void OnTransactionRecordItemClick(TxItem item);
    }
}
