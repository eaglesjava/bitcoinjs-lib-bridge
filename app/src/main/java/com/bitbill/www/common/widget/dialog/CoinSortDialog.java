package com.bitbill.www.common.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.entity.TabItem;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isanwenyu on 2018/3/27.
 */

public class CoinSortDialog extends BaseDialog implements BaseViewControl {

    public static final String TAG = "CoinSortDialog";
    @BindView(R.id.list)
    RecyclerView mList;
    private List<TabItem> datas;
    private CommonAdapter<TabItem> mAdapter;
    private OnSortItemClickListener mOnSortItemClickListener;

    public static CoinSortDialog newInstance(List<TabItem> datas) {

        Bundle args = new Bundle();
        args.putSerializable(AppConstants.ARG_DATAS, (Serializable) datas);

        CoinSortDialog fragment = new CoinSortDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBeforeSetContentLayout();
        View view = inflater.inflate(getLayoutId(), container);
        setUnBinder(ButterKnife.bind(this, view));
        init(savedInstanceState);
        initView();
        initData();
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        return view;
    }

    @Override
    public void onBeforeSetContentLayout() {

        datas = (List<TabItem>) getArguments().getSerializable(AppConstants.ARG_DATAS);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mAdapter = new CommonAdapter<TabItem>(getBaseActivity(), R.layout.item_dialog_coin_sort, datas) {

            @Override
            protected void convert(ViewHolder holder, TabItem item, int position) {
                holder.setText(R.id.tv_coin_symbol, item.getSymbol());
                holder.setText(R.id.tv_coin_name, item.getCoinId());
                holder.setChecked(R.id.rb_selector, !item.isHide());
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mOnSortItemClickListener != null) {
                    mOnSortItemClickListener.onSortItemClick(position);
                }
                ViewHolder viewHolder = (ViewHolder) holder;
                boolean checked = !((Checkable) viewHolder.getView(R.id.rb_selector)).isChecked();
                viewHolder.setChecked(R.id.rb_selector, checked);
                TabItem tabItem = datas.get(position);
                tabItem.setHide(checked);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new DividerDecoration(getBaseActivity(), DividerDecoration.VERTICAL_LIST).setDivider(getResources().getDrawable(R.color.item_line)));


    }

    @Override
    public void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_coin_sort;
    }

    public CoinSortDialog setOnSortItemClickListener(OnSortItemClickListener onSortItemClickListener) {
        mOnSortItemClickListener = onSortItemClickListener;
        return this;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public interface OnSortItemClickListener {
        void onSortItemClick(int position);
    }
}
