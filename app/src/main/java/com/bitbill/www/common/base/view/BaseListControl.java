package com.bitbill.www.common.base.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.bitbill.www.common.base.model.entity.Entity;
import com.bitbill.www.common.widget.decoration.DividerDecoration;

/**
 * Created by isanwenyu on 2017/12/27.
 */

interface BaseListControl<E extends Entity> {
    void initRecyclerView();

    @NonNull
    DividerDecoration getDecoration();

    RecyclerView.Adapter getAdapter();

    void setAdapter(RecyclerView.Adapter adapter);

    void notifyDataSetChanged();

    void notifyItemChanged(int position);

    RecyclerView.LayoutManager getLayoutManager();

    void initRefreshLayout();

    void onLayoutRefresh();

    boolean isEnableRefresh();

    int getRefreshSchemeColor();

    void clearData();
}
