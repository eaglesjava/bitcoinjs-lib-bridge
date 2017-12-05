package com.bitbill.www.ui.wallet.info;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.R;
import com.bitbill.www.model.wallet.network.entity.TransactionRecord;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTransactionRecordItemClickListener}
 * interface.
 */
public class BtcRecordFragment extends Fragment {

    private OnTransactionRecordItemClickListener mListener;
    private List<TransactionRecord> mRecordList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BtcRecordFragment() {
    }

    public static BtcRecordFragment newInstance() {
        BtcRecordFragment fragment = new BtcRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
        // TODO: 2017/12/5 获取列表数据
        mRecordList = new ArrayList();
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..sRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(1, "1PN9ET1..sRaqfPN", "2017.11.10 15:32", 0, 235));
        mRecordList.add(new TransactionRecord(0, "1PN9ET1..sRaqfPN", "2017.11.10 15:32", 0, 235));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btcrecord_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CommonAdapter<TransactionRecord>(context, R.layout.item_btc_record, mRecordList) {

                @Override
                protected void convert(ViewHolder holder, TransactionRecord transactionRecord, int position) {
                    holder.setText(R.id.tv_address, transactionRecord.getAddress());
                    holder.setText(R.id.tv_amount, (transactionRecord.getStatus() == 0 ? "+" : "-") + transactionRecord.getAmount() + " btc");
                    holder.setText(R.id.tv_date, transactionRecord.getDate() + "  " + transactionRecord.getConfirmCount() + "确认");
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
            });
        }
        return view;
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
