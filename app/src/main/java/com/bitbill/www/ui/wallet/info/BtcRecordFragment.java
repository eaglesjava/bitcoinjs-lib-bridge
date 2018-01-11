package com.bitbill.www.ui.wallet.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseLazyListFragment;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.Decoration;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.db.entity.Wallet;
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
public class BtcRecordFragment extends BaseLazyListFragment<TxRecord, BtcRecordMvpPresenter> implements BtcRecordMvpView, ParseTxInfoMvpView {

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amount_label)
    TextView tvAmountLabel;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcCny;
    @BindView(R.id.tv_btc_unconfirm)
    TextView tvBtcUnconfirm;
    @Inject
    BtcRecordMvpPresenter<TxModel, BtcRecordMvpView> mBtcRecordMvpPresenter;
    @Inject
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> mViewParseTxInfoMvpPresenter;
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
    protected void onListItemClick(TxRecord txRecord, int position) {
        if (mListener != null) {
            mListener.OnTransactionRecordItemClick(txRecord);
        }
    }

    @NonNull
    @Override
    public DividerDecoration getDecoration() {
        return new DividerDecoration(getBaseActivity(), Decoration.VERTICAL);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_btc_record;
    }

    @Override
    protected void itemConvert(ViewHolder holder, TxRecord txRecord, int position) {
        String inOutString = txRecord.getInOut() == TxRecord.InOut.TRANSFER ? "" : (txRecord.getInOut() == TxRecord.InOut.IN ? "+" : "-");
        holder.setText(R.id.tv_amount, inOutString + StringUtils.satoshi2btc(txRecord.getSumAmount()) + " btc");


        holder.setText(R.id.tv_date, StringUtils.formatDate(txRecord.getCreatedTime()));
        if (txRecord.getHeight() == -1) {
            holder.setImageResource(R.id.iv_status, R.drawable.ic_item_unconfirm);
            holder.setText(R.id.tv_status, getString(R.string.status_item_unconfirm));
        } else {
            switch (txRecord.getInOut()) {
                case TRANSFER:
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_transfer);
                    holder.setText(R.id.tv_status, getString(R.string.status_item_transfer));
                    break;
                case IN:
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_receive);
                    holder.setText(R.id.tv_status, getString(R.string.status_item_receive));
                    break;
                case OUT:
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_send);
                    holder.setText(R.id.tv_status, getString(R.string.status_item_send));
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
    public boolean isEnableRefresh() {
        return true;
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        setRefresh(false);
    }

    @Override
    public void onBeforeSetContentLayout() {
        mWalelt = (Wallet) getArguments().getSerializable(AppConstants.ARG_WALLET);
        mWalelt.__setDaoSession(getApp().getDaoSession());


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
    public long getConfrimId() {
        return 0;
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
    public void parsedTxItemList(List<TxRecord> txRecords) {
        setDatas(txRecords);
        setRefresh(false);
    }

    @Override
    public void parsedTxItemListFail() {
        showMessage(R.string.fail_parse_tx_item_list);
        setRefresh(false);
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
        void OnTransactionRecordItemClick(TxRecord item);
    }
}
