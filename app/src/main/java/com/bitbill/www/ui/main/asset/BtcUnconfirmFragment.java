package com.bitbill.www.ui.main.asset;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
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
public class BtcUnconfirmFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_in_progress)
    TextView tvInProgress;
    private OnTransactionRecordItemClickListener mListener;
    private List<TransactionRecord> mRecordList;
    private CommonAdapter<TransactionRecord> mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BtcUnconfirmFragment() {
    }

    public static BtcUnconfirmFragment newInstance() {
        BtcUnconfirmFragment fragment = new BtcUnconfirmFragment();
        Bundle args = new Bundle();
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
        if (mRecordList == null) {
            mRecordList = new ArrayList();
        }

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        // Set the adapter
        mAdapter = new CommonAdapter<TransactionRecord>(getBaseActivity(), R.layout.item_btc_record_unconfirm, mRecordList) {

            @Override
            protected void convert(ViewHolder holder, TransactionRecord transactionRecord, int position) {
                holder.setText(R.id.tv_address, transactionRecord.getAddress());
                holder.setText(R.id.tv_amount, (transactionRecord.getStatus() == 0 ? "+" : "-") + transactionRecord.getAmount() + " btc");
                holder.setText(R.id.tv_date, transactionRecord.getDate());
                holder.setImageResource(R.id.iv_status, transactionRecord.getStatus() == 0 ? R.drawable.ic_item_receive : R.drawable.ic_item_send);
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
    }

    @Override
    public void initData() {
        // TODO: 2017/12/5 获取列表数据
        mRecordList.clear();
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(1, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..dfaDFDsRaqfPN", "2017.11.10 15:32", 0, 235));
        tvInProgress.setText(String.format(getString(R.string.text_in_progress_count), mRecordList.size()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_unconfirm_list;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
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
