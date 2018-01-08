package com.bitbill.www.ui.main.asset;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseListFragment;
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
public class BtcUnconfirmFragment extends BaseListFragment<TxItem, MvpPresenter> {

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
    public int getItemLayoutId() {
        return R.layout.item_btc_record_unconfirm;
    }

    @Override
    protected void onListItemClick(TxItem txItem, int position) {
        if (mListener != null) {
            mListener.OnTransactionRecordItemClick(txItem);
        }
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
            holder.setText(R.id.tv_confirm_count, confirmCount > 1000 ? "1000+" : String.valueOf(confirmCount) + "确认");
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
    public void onBeforeSetContentLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        mUnconfirmList = (List<TxItem>) getArguments().getSerializable(ARG_UNCONFIRM_LIST);
        if (mUnconfirmList == null) {
            mUnconfirmList = new ArrayList();
        }
        setDatas(mUnconfirmList);
        tvInProgress.setText(String.format(getString(R.string.text_in_progress_count), mUnconfirmList.size()));
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
