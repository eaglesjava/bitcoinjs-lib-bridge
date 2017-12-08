package com.bitbill.www.ui.main.receive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseLazyFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BtcReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BtcReceiveFragment extends BaseLazyFragment {


    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_receive_amount)
    TextView tvReceiveAmount;

    public BtcReceiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BtcReceiveFragment.
     */
    public static BtcReceiveFragment newInstance() {
        BtcReceiveFragment fragment = new BtcReceiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        tvReceiveAmount.setText(Html.fromHtml(getString(R.string.text_receive_specific_amount)));
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_receive;
    }

    /**
     * 懒加载数据
     * 在onFirstUserVisible之后
     */
    @Override
    public void lazyData() {
    }

}
