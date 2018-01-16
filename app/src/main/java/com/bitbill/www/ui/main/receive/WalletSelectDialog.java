package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.R;
import com.bitbill.www.app.BitbillApp;
import com.bitbill.www.common.base.view.BaseViewControl;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.Decoration;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isanwenyu@163.com on 2017/12/9.
 */
public class WalletSelectDialog extends BottomSheetDialogFragment implements BaseViewControl {
    public static final String TAG = "WalletSelectDialog";
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    private CommonAdapter<Wallet> mAdapter;
    private List<Wallet> mWalletList;
    private int mSelectedPos = -1;
    private OnWalletSelectItemClickListener mOnWalletSelectItemClickListener;
    private Wallet mSelectedWallet;
    private BitbillApp mApp;

    public static WalletSelectDialog newInstance() {

        Bundle args = new Bundle();
        WalletSelectDialog fragment = new WalletSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = BitbillApp.get();
    }

    @Override
    public BitbillApp getApp() {
        return mApp;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBeforeSetContentLayout();
        View view = inflater.inflate(getLayoutId(), container);
        ButterKnife.bind(this, view);
        init(savedInstanceState);
        //init view
        initView();
        initData();
        return view;
    }


    @Override
    public void onBeforeSetContentLayout() {
        mWalletList = BitbillApp.get().getWallets();
        if (StringUtils.isEmpty(mWalletList)) return;

        //  通过钱包对象设置选择后的当前位置
        mSelectedPos = mWalletList.indexOf(getSelectedWallet());


    }

    @Nullable
    private Wallet getSelectedWallet() {
        if (StringUtils.isEmpty(mWalletList)) return null;
        Wallet selectedWallet = null;
        for (Wallet wallet : mWalletList) {
            if (wallet.isSelected()) {
                selectedWallet = wallet;
                return selectedWallet;
            }
        }
        return selectedWallet;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

        Decoration decor = new Decoration(getContext(), Decoration.VERTICAL);
        mRecyclerView.addItemDecoration(decor);

        mAdapter = new CommonAdapter<Wallet>(getContext(), R.layout.item_wallet_select_view, mWalletList) {

            @Override
            protected void convert(ViewHolder holder, Wallet wallet, final int position) {

                holder.setText(R.id.tv_wallet_name, StringUtils.cutWalletName(wallet.getName()));
                holder.setText(R.id.tv_wallet_amount, StringUtils.satoshi2btc(wallet.getBalance()) + " btc");
                holder.setText(R.id.tv_wallet_label, String.valueOf(wallet.getName().charAt(0)));

                holder.setChecked(R.id.rb_selector, wallet.isSelected());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //实现单选方法三： RecyclerView另一种定向刷新方法：不会有白光一闪动画 也不会重复onBindVIewHolder
                        //如果勾选的不是已经勾选状态的Item
                        if (mSelectedPos != position && mSelectedPos != -1) {
                            //先取消上个item的勾选状态
                            ViewHolder commonHolder = ((ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mSelectedPos));
                            if (commonHolder != null) {//还在屏幕里
                                commonHolder.setChecked(R.id.rb_selector, false);
                            } else {
                                //add by 2016 11 22 for 一些极端情况，holder被缓存在Recycler的cacheView里，
                                //此时拿不到ViewHolder，但是也不会回调onBindViewHolder方法。所以add一个异常处理
                                notifyItemChanged(mSelectedPos);
                            }
                            mWalletList.get(mSelectedPos).setSelected(false);//不管在不在屏幕里 都需要改变数据
                            //设置新Item的勾选状态
                            mSelectedPos = position;
                            mWalletList.get(mSelectedPos).setSelected(true);
                            holder.setChecked(R.id.rb_selector, wallet.isSelected());
                            if (mOnWalletSelectItemClickListener != null) {
                                mOnWalletSelectItemClickListener.onItemSelected(wallet, position);
                            }
                        }
                        dismiss();
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_wallet_select;
    }


    public WalletSelectDialog setOnWalletSelectItemClickListener(OnWalletSelectItemClickListener onWalletSelectItemClickListener) {
        mOnWalletSelectItemClickListener = onWalletSelectItemClickListener;
        return this;
    }

    public interface OnWalletSelectItemClickListener {
        void onItemSelected(Wallet selectedWallet, int position);
    }
}
