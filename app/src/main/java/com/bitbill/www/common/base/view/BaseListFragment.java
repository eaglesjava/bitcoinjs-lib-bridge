package com.bitbill.www.common.base.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.CustomSwipeToRefresh;
import com.bitbill.www.common.widget.Decoration;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by isanwenyu on 2017/12/27.
 */

public abstract class BaseListFragment<E extends Serializable, P extends MvpPresenter> extends BaseFragment<P> implements BaseListControl {

    @BindView(R.id.list)
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected List<E> mDatas;
    @BindView(R.id.refresh_layout)
    CustomSwipeToRefresh mRefreshLayout;

    protected abstract void onListItemClick(E e, int position);

    protected abstract void itemConvert(ViewHolder holder, E e, int position);

    @Override
    public void onBeforeSetContentLayout() {
        mDatas = new ArrayList<>();
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        initRefreshLayout();
        initRecyclerView();
    }

    @Override
    public void initRefreshLayout() {
        if (!isEnableRefresh()) {
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onLayoutRefresh();
                }
            });
            if (getRefreshSchemeColor() != 0) {
                mRefreshLayout.setColorSchemeResources(getRefreshSchemeColor());
            }
        }


    }

    @Override
    public void onLayoutRefresh() {

    }

    @Override
    public void initRecyclerView() {
        if (getLayoutManager() == null || getItemLayoutId() == 0) {
            new RuntimeException("must implement getLayoutManager or getItemLayoutId");
            return;
        }
        mRecyclerView.setLayoutManager(getLayoutManager());
        if (getDecoration() != null) {
            mRecyclerView.addItemDecoration(getDecoration());
        }
        // Set the adapter
        mAdapter = new CommonAdapter<E>(getBaseActivity(), getItemLayoutId(), mDatas) {

            @Override
            protected void convert(ViewHolder holder, E e, int position) {
                itemConvert(holder, e, position);
                holder.itemView.setOnClickListener(v -> {
                    onListItemClick(e, position);
                });
            }
        };
        setAdapter(mAdapter);
    }


    @Override
    @NonNull
    public DividerDecoration getDecoration() {
        return new DividerDecoration(getBaseActivity(), Decoration.VERTICAL);
    }

    public void setDatas(List<E> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();

    }

    protected boolean isDataEmpty() {
        return StringUtils.isEmpty(mDatas);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    protected void notifyItemRemoved(int position) {
        mDatas.remove(position);//删除数据源
        mAdapter.notifyItemRemoved(position);//刷新被删除的地方
        mAdapter.notifyItemRangeChanged(position, getItemCount());//刷新被删除数据，以及其后面的数据

    }

    private int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getBaseActivity());
    }

    @Override
    public int getItemLayoutId() {
        return 0;
    }

    @Override
    public int getRefreshSchemeColor() {
        return 0;
    }

    @Override
    public boolean isEnableRefresh() {
        return false;
    }
}
