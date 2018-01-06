package com.bitbill.www.ui.main.asset;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.network.entity.TxItem;
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

    public static final String TAG = BtcUnconfirmFragment.class.getSimpleName();
    private static final String ARG_UNCONFIRM_LIST = "arg_unconfirm_list";
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_in_progress)
    TextView tvInProgress;
    private OnTransactionRecordItemClickListener mListener;
    private List<TxItem> mUnconfirmList;
    private CommonAdapter<TxItem> mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BtcUnconfirmFragment() {
    }

    public static BtcUnconfirmFragment newInstance(ArrayList<TxItem> unconfirmList) {
        BtcUnconfirmFragment fragment = new BtcUnconfirmFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UNCONFIRM_LIST, unconfirmList);
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
        mUnconfirmList = (List<TxItem>) getArguments().getSerializable(ARG_UNCONFIRM_LIST);
        if (mUnconfirmList == null) {
            mUnconfirmList = new ArrayList();
        }

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        // Set the adapter
        mAdapter = new CommonAdapter<TxItem>(getBaseActivity(), R.layout.item_btc_record_unconfirm, mUnconfirmList) {

            @Override
            protected void convert(ViewHolder holder, TxItem unconfirm, int position) {
                holder.setText(R.id.tv_address, unconfirm.getGatherAddressOut());
                String inOutString = unconfirm.getInOut() == TxItem.InOut.TRANSFER ? "" : (unconfirm.getInOut() == TxItem.InOut.IN ? "+" : "-");
                holder.setText(R.id.tv_amount, inOutString + StringUtils.satoshi2btc(unconfirm.getSumAmount()) + " btc");
                long confirmCount = BitbillApp.get().getBlockHeight() - unconfirm.getHeight() + 1;
                holder.setAlpha(R.id.tv_confirm_count, confirmCount > 0 ? 1.0f : 0.6f);
                holder.setText(R.id.tv_confirm_count, confirmCount + "确认");
                holder.setText(R.id.tv_date, unconfirm.getCreatedTime());
                if (unconfirm.getHeight() == -1) {
                    holder.setImageResource(R.id.iv_status, R.drawable.ic_item_unconfirm);
                } else {
                    switch (unconfirm.getInOut()) {
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
                holder.setOnClickListener(R.id.rl_item_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.OnTransactionRecordItemClick(unconfirm);
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        tvInProgress.setText(String.format(getString(R.string.text_in_progress_count), mUnconfirmList.size()));
    }

    @Override
    public void initData() {
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
        void OnTransactionRecordItemClick(TxItem item);
    }
}
