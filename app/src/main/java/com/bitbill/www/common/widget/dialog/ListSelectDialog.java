package com.bitbill.www.common.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isanwenyu on 2017/12/20.
 */

public class ListSelectDialog extends BaseDialog implements BaseViewControl {

    public static final String TAG = "ListSelectDialog";
    @BindView(R.id.list)
    RecyclerView mList;
    private String[] datas;
    private CommonAdapter<String> mAdapter;
    private OnListSelectItemClickListener mOnListSelectItemClickListener;

    public static ListSelectDialog newInstance(String[] datas) {

        Bundle args = new Bundle();
        args.putStringArray(AppConstants.ARG_DATAS, datas);

        ListSelectDialog fragment = new ListSelectDialog();
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
        return view;
    }

    @Override
    public void onBeforeSetContentLayout() {

        datas = getArguments().getStringArray(AppConstants.ARG_DATAS);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mAdapter = new CommonAdapter<String>(getBaseActivity(), R.layout.item_dialog_list, Arrays.asList(datas)) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_dialog_select, s);
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mOnListSelectItemClickListener != null) {
                    mOnListSelectItemClickListener.onSelectItemClick(position);
                }
                dismissDialog(TAG);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new DividerDecoration(getBaseActivity(), DividerDecoration.VERTICAL_LIST).setDividerPadding(0).setDivider(getResources().getDrawable(R.color.item_line)));


    }

    @Override
    public void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_list_select;
    }

    public ListSelectDialog setOnListSelectItemClickListener(OnListSelectItemClickListener onListSelectItemClickListener) {
        mOnListSelectItemClickListener = onListSelectItemClickListener;
        return this;
    }

    public interface OnListSelectItemClickListener {
        void onSelectItemClick(int position);
    }
}
