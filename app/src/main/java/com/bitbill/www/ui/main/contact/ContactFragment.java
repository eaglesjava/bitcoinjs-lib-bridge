package com.bitbill.www.ui.main.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.view.BaseListFragment;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.decoration.DividerDecoration;
import com.bitbill.www.common.widget.dialog.ListSelectDialog;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.ui.main.send.ContactSelectActivity;
import com.bitbill.www.ui.main.send.ScanQrcodeActivity;
import com.mcxtzhang.indexlib.IndexBar.utils.IndexHelper;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.util.List;

import javax.inject.Inject;

public class ContactFragment extends BaseListFragment<Contact, ContactMvpPresenter> implements ContactMvpView {

    @Inject
    ContactMvpPresenter<ContactModel, ContactMvpView> mContactMvpPresenter;
    private ListSelectDialog mListSelectDialog;
    private EmptyWrapper mEmptyWrapper;
    private boolean isSelect;

    public static ContactFragment newInstance(boolean isSelect) {

        Bundle args = new Bundle();
        args.putBoolean(AppConstants.ARG_IS_SELECT, isSelect);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        isSelect = getArguments().getBoolean(AppConstants.ARG_IS_SELECT);
    }

    @Override
    public ContactMvpPresenter getMvpPresenter() {
        return mContactMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_contact_view;
    }

    @Override
    protected void onListItemClick(Contact contact, int position) {
        if (isSelect) {
            ((ContactSelectActivity) getBaseActivity()).finishSelect(contact);
        } else {
            //跳转到联系人详情页面
            ContactDetailActivity.start(getBaseActivity(), mDatas.get(position));
        }
    }

    @Override
    protected void itemConvert(ViewHolder holder, Contact contact, int position) {
        holder.setText(R.id.tv_contact_name, contact.getContactName());
        holder.setText(R.id.tv_contact_address, StringUtils.isEmpty(contact.getWalletId()) ? contact.getAddress() : contact.getWalletId());
        holder.setText(R.id.tv_contact_label, StringUtils.getNameLabel(contact.getContactName()));
        if (position == 0) {//等于0肯定要有title的
            holder.setText(R.id.tv_label_title, contact.getBaseIndexTag());

        } else {//其他的通过判断
            if (null != mDatas.get(position).getSuspensionTag() && !contact.getSuspensionTag().equals(mDatas.get(position - 1).getSuspensionTag())) {
                //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                holder.setText(R.id.tv_label_title, mDatas.get(position).getBaseIndexTag());
            } else {
                //none
                holder.setText(R.id.tv_label_title, "");
            }
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mEmptyWrapper = new EmptyWrapper(getAdapter());
        View emptyView = LayoutInflater.from(getBaseActivity()).inflate(R.layout.layout_contact_empty_view, mRecyclerView, false);
        emptyView.findViewById(R.id.btn_add_now).setOnClickListener(v -> showSelectDialog());
        mEmptyWrapper.setEmptyView(emptyView);
        super.setAdapter(mEmptyWrapper);
    }

    @NonNull
    @Override
    public DividerDecoration getDecoration() {
        //如果add两个，那么按照先后顺序，依次渲染。
        int dividerLeftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
        return new DividerDecoration(getBaseActivity(), DividerDecoration.VERTICAL_LIST).setDividerLeftPadding(dividerLeftPadding);
    }

    @Override
    public void initView() {
        super.initView();

        mListSelectDialog = ListSelectDialog.newInstance(getResources().getStringArray(R.array.dialog_create_contact));
        mListSelectDialog.setOnListSelectItemClickListener(position -> {
            switch (position) {
                case 0:
                    //通过id添加
                    AddContactByIdActivity.start(getBaseActivity());
                    break;
                case 1:
                    //通过地址添加
                    AddContactByAddressActivity.start(getBaseActivity());
                    break;
                case 2:
                    //扫码添加
                    ScanQrcodeActivity.start(getBaseActivity());
                    break;

            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void initData() {
        getMvpPresenter().loadContact();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    public void showSelectDialog() {
        // 跳出选择对话框
        mListSelectDialog.show(getChildFragmentManager(), ListSelectDialog.TAG);
    }

    @Override
    public void loadContactSuccess(List<Contact> contacts) {
        if (StringUtils.isEmpty(contacts)) {
            clearData();
        } else {
            //对已有数据进行排序 创建索引
            new IndexHelper(contacts);
            setDatas(contacts);
        }

    }

    @Override
    public void loadContactFail() {

    }
}
