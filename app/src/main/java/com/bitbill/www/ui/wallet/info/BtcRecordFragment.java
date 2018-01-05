package com.bitbill.www.ui.wallet.info;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseLazyFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.CustomSwipeToRefresh;
import com.bitbill.www.common.widget.Decoration;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.TransactionRecord;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTransactionRecordItemClickListener}
 * interface.
 */
public class BtcRecordFragment extends BaseLazyFragment {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amount_label)
    TextView tvAmountLabel;
    @BindView(R.id.refresh_layout)
    CustomSwipeToRefresh refreshLayout;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcCny;
    @BindView(R.id.tv_btc_unconfirm)
    TextView tvBtcUnconfirm;
    private OnTransactionRecordItemClickListener mListener;
    private List<TransactionRecord> mRecordList;
    private CommonAdapter<TransactionRecord> mAdapter;
    private Wallet mWalelt;

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
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void onBeforeSetContentLayout() {
        mWalelt = (Wallet) getArguments().getSerializable(AppConstants.ARG_WALLET);
        if (mRecordList == null) {
            mRecordList = new ArrayList();
        }

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lazyData();
            }
        });
        refreshLayout.setColorSchemeResources(R.color.blue);
        Decoration decor = new Decoration(getBaseActivity(), Decoration.VERTICAL);
        mRecyclerView.addItemDecoration(decor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        // Set the adapter
        mAdapter = new CommonAdapter<TransactionRecord>(getBaseActivity(), R.layout.item_btc_record, mRecordList) {

            @Override
            protected void convert(ViewHolder holder, TransactionRecord transactionRecord, int position) {
                holder.setText(R.id.tv_address, transactionRecord.getAddress());
                holder.setText(R.id.tv_amount, (transactionRecord.getStatus() == 0 ? "+" : "-") + transactionRecord.getAmount() + " btc");
                holder.setText(R.id.tv_date, StringUtils.formatDate(transactionRecord.getDate()) + "  " + (transactionRecord.getConfirmCount() == 0 ? "未确认" : transactionRecord.getConfirmCount() + " 确认"));
                holder.setImageResource(R.id.iv_status, transactionRecord.getConfirmCount() == 0 ? R.drawable.ic_item_unconfirm : (transactionRecord.getStatus() == 0 ? R.drawable.ic_item_receive : R.drawable.ic_item_send));
                holder.setOnClickListener(R.id.rl_item_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.OnTransactionRecordItemClick(transactionRecord);
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
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

        // TODO: 2017/12/5 获取列表数据
        mRecordList.clear();
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(1, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 2, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 1, 235));
        mRecordList.add(new TransactionRecord(1, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 4, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 5, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 2, 235));
        mRecordList.add(new TransactionRecord(1, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        refreshLayout.setRefreshing(false);

        if (mWalelt != null) {
            tvAmount.setText(StringUtils.satoshi2btc(mWalelt.getBalance()));
            tvBtcUnconfirm.setText(String.format(getString(R.string.text_btc_unconfirm), StringUtils.satoshi2btc(mWalelt.getUnconfirm())));

        }
        tvBtcCny.setText(BitbillApp.get().getBtcValue(StringUtils.satoshi2btc(mWalelt.getBalance())));
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
        void OnTransactionRecordItemClick(TransactionRecord item);
    }
}
