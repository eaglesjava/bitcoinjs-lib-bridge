package com.bitbill.www.ui.main.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.base.view.decoration.DividerDecoration;
import com.bitbill.www.common.base.view.dialog.ListSelectDialog;
import com.bitbill.www.model.contact.network.entity.Contact;
import com.mcxtzhang.indexlib.IndexBar.utils.IndexHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ContactActivity extends BaseToolbarActivity {
    @BindView(R.id.rv)
    RecyclerView mRv;


    CommonAdapter<Contact> mAdapter;
    LinearLayoutManager mManager;
    List<Contact> mDatas;
    private ListSelectDialog mListSelectDialog;
    private EmptyWrapper mEmptyWrapper;

    public static void start(Context context) {
        Intent starter = new Intent(context, ContactActivity.class);
        context.startActivity(starter);
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
// TODO: 2017/12/20 test data
        mDatas = new ArrayList<>();

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mAdapter = new CommonAdapter<Contact>(this, R.layout.item_contact_view, mDatas) {
            @Override
            protected void convert(ViewHolder holder, Contact contact, int position) {
                holder.setText(R.id.tv_contact_name, contact.getName());
                holder.setText(R.id.tv_contact_address, contact.getAddress());
                holder.setText(R.id.tv_contact_label, String.valueOf(contact.getName().charAt(0)));
                if (position == 0) {//等于0肯定要有title的
                    holder.setText(R.id.tv_label_title, mDatas.get(position).getBaseIndexTag());

                } else {//其他的通过判断
                    if (null != mDatas.get(position).getSuspensionTag() && !mDatas.get(position).getSuspensionTag().equals(mDatas.get(position - 1).getSuspensionTag())) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        holder.setText(R.id.tv_label_title, mDatas.get(position).getBaseIndexTag());
                    } else {
                        //none
                        holder.setText(R.id.tv_label_title, "");
                    }
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转到联系人详情页面
                ContactDetailActivity.start(ContactActivity.this, mDatas.get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mEmptyWrapper = new EmptyWrapper(mAdapter);

        mEmptyWrapper.setEmptyView(R.layout.layout_contact_empty_view);

        mRv.setAdapter(mEmptyWrapper);

        //如果add两个，那么按照先后顺序，依次渲染。
        int dividerLeftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
        mRv.addItemDecoration(new DividerDecoration(this, DividerDecoration.VERTICAL_LIST).setDividerLeftPadding(dividerLeftPadding));

        //对已有数据进行排序 创建索引
        new IndexHelper(mDatas);

        mListSelectDialog = ListSelectDialog.newInstance(getResources().getStringArray(R.array.dialog_create_contact));
        mListSelectDialog.setOnListSelectItemClickListener(position -> {
            switch (position) {
                case 0:
                    //通过id添加
                    AddContactByIdActivity.start(ContactActivity.this);
                    break;
                case 1:
                    //通过地址添加
                    showMessage("通过地址添加");
                    break;
                case 2:
                    //扫码添加
                    showMessage("扫码添加");
                    break;
            }
        });

    }

    @Override
    public void initData() {


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact_create) {
            showSelectDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSelectDialog() {
        // 跳出选择对话框
        mListSelectDialog.show(getSupportFragmentManager(), ListSelectDialog.TAG);
    }

    public void addNowClick(View view) {
        showSelectDialog();
    }
}
