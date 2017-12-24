package com.bitbill.www.common.base.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Wallet view
 */
public class SelectWalletView extends FrameLayout implements View.OnClickListener {
    public static final int NORMAL_COLOR_ID = R.color.blue;
    public static final int BACKUP_COLOR_ID = R.color.red;
    public static final int NORMAL_BG_ID = R.drawable.btn_blue;
    private static final int DEFAULT_PADDING = 15;//unit dp
    @BindView(R.id.tv_unbackuped)
    TextView tvUnbackupedTextView;
    @BindView(R.id.tv_wallet_label)
    TextView tvWalletLabel;
    @BindView(R.id.tv_wallet_name)
    TextView tvWalletName;
    @BindView(R.id.tv_wallet_amount)
    TextView tvWalletAmount;
    @BindView(R.id.iv_right_arrow)
    ImageView ivRightArrow;
    private int mNormalColor;
    private int mBackupColor;
    private Drawable mNormalBackground;

    private String mWalletName;
    private String mWalletAmount;
    private boolean backuped;
    private String mWalletLabel;
    private SelectWalletView.OnWalletClickListener mOnWalletClickListener;
    private Wallet mWallet;

    public SelectWalletView(Context context) {
        super(context);
        init(null, 0);
    }

    public SelectWalletView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SelectWalletView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SelectWalletView, defStyle, 0);

        mWalletLabel = a.getString(
                R.styleable.SelectWalletView_selectWalletLabel);
        mWalletName = a.getString(
                R.styleable.SelectWalletView_selectWalletName);
        mNormalColor = a.getColor(
                R.styleable.SelectWalletView_selectNormalColor,
                getResources().getColor(NORMAL_COLOR_ID));
        mBackupColor = a.getColor(
                R.styleable.SelectWalletView_selectBackupColor,
                getResources().getColor(BACKUP_COLOR_ID));
        mWalletAmount = a.getString(
                R.styleable.SelectWalletView_selectWalletAmount);
        backuped = a.getBoolean(
                R.styleable.SelectWalletView_selectBackuped, backuped);
        backuped = a.getBoolean(
                R.styleable.SelectWalletView_selectBackuped, backuped);
        if (a.hasValue(R.styleable.SelectWalletView_selectNormalBackground)) {
            mNormalBackground = a.getDrawable(
                    R.styleable.SelectWalletView_selectNormalBackground);
            mNormalBackground.setCallback(this);
        } else {
            mNormalBackground = getResources().getDrawable(NORMAL_BG_ID);
        }

        a.recycle();

        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_select_wallet_view, this);
        ButterKnife.bind(this);
        setBackground(mNormalBackground);
        setBackuped(backuped)
                .setWalletName(mWalletName)
                .setWalletLabel(mWalletLabel)
                .setWalletAmount(mWalletAmount);
        setOnClickListener(this);
    }

    public SelectWalletView setWalletName(String walletName) {
        mWalletName = walletName;
        tvWalletName.setText(walletName);
        return this;
    }

    public SelectWalletView setWalletAmount(String walletAmount) {
        mWalletAmount = walletAmount;
        tvWalletAmount.setText(walletAmount);
        return this;
    }

    public SelectWalletView setWalletLabel(String walletLabel) {
        mWalletLabel = walletLabel;
        tvWalletLabel.setText(walletLabel);
        return this;
    }

    public SelectWalletView setBackuped(boolean backuped) {
        this.backuped = backuped;
        refreshLayout();
        return this;
    }

    private void refreshLayout() {
        if (!backuped) {
            tvUnbackupedTextView.setVisibility(View.VISIBLE);
        } else {
            tvUnbackupedTextView.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.tv_unbackuped)
    public void onViewClicked() {
        if (mOnWalletClickListener != null) {
            mOnWalletClickListener.onBackupClick(getWallet(), tvUnbackupedTextView);
        }

    }

    public Wallet getWallet() {
        return mWallet;
    }

    public SelectWalletView setWallet(Wallet wallet) {
        mWallet = wallet;
        //填充布局数据
        this.setWalletName(StringUtils.cutWalletName(wallet.getName()))
                .setWalletLabel(String.valueOf(wallet.getName().charAt(0)))
                // TODO: 2017/11/28 从后台获余额
                .setWalletAmount(StringUtils.satoshi2btc(wallet.getBtcBalance()) + " btc")
                .setBackuped(wallet.getIsBackuped());
        return this;
    }

    public SelectWalletView setOnWalletClickListener(SelectWalletView.OnWalletClickListener onWalletClickListener) {
        mOnWalletClickListener = onWalletClickListener;
        return this;
    }

    public SelectWalletView setRightArrowClickListener(OnClickListener onClickListener) {
        ivRightArrow.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mOnWalletClickListener != null) {
            mOnWalletClickListener.onWalletClick(getWallet(), v);
        }
    }

    public interface OnWalletClickListener {

        void onWalletClick(Wallet wallet, View view);

        void onBackupClick(Wallet wallet, View view);
    }


}
