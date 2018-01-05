package com.bitbill.www.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
public class WalletView extends LinearLayout implements View.OnClickListener {
    public static final int NORMAL_COLOR_ID = R.color.blue;
    public static final int BACKUP_COLOR_ID = R.color.red;
    public static final int NORMAL_BG_ID = R.drawable.bg_blue_corner;
    public static final int BACKUP_BG_ID = R.drawable.bg_red_corner;
    private static final int DEFAULT_PADDING = 15;//unit dp
    @BindView(R.id.tv_wallet_label)
    TextView tvWalletLabel;
    @BindView(R.id.tv_wallet_name)
    TextView tvWalletName;
    @BindView(R.id.tv_wallet_amount)
    TextView tvWalletAmount;
    @BindView(R.id.btn_backup_now)
    Button btnBackupNow;
    private int mNormalColor;
    private int mBackupColor;
    private Drawable mNormalBackground;
    private Drawable mBackupBackground;
    private Drawable mSelectBackground;

    private String mWalletName;
    private String mWalletAmount;
    private boolean backuped;
    private String mWalletLabel;
    private OnWalletClickListener mOnWalletClickListener;
    private Wallet mWallet;

    public WalletView(Context context) {
        super(context);
        init(null, 0);
    }

    public WalletView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WalletView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WalletView, defStyle, 0);

        mWalletLabel = a.getString(
                R.styleable.WalletView_walletLabel);
        mWalletName = a.getString(
                R.styleable.WalletView_walletName);
        mNormalColor = a.getColor(
                R.styleable.WalletView_normalColor,
                getResources().getColor(NORMAL_COLOR_ID));
        mBackupColor = a.getColor(
                R.styleable.WalletView_backupColor,
                getResources().getColor(BACKUP_COLOR_ID));
        mWalletAmount = a.getString(
                R.styleable.WalletView_walletAmount);
        backuped = a.getBoolean(
                R.styleable.WalletView_backuped, backuped);
        if (a.hasValue(R.styleable.WalletView_normalBackground)) {
            mNormalBackground = a.getDrawable(
                    R.styleable.WalletView_normalBackground);
            mNormalBackground.setCallback(this);
        } else {
            mNormalBackground = getResources().getDrawable(NORMAL_BG_ID);
        }
        if (a.hasValue(R.styleable.WalletView_backupBackground)) {
            mBackupBackground = a.getDrawable(
                    R.styleable.WalletView_backupBackground);
            mBackupBackground.setCallback(this);
        } else {
            mBackupBackground = getResources().getDrawable(BACKUP_BG_ID);
        }

        a.recycle();

        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        inflate(getContext(), R.layout.layout_wallet_view, this);
        ButterKnife.bind(this);
        setBackground(mNormalBackground);
        setBackuped(backuped)
                .setWalletName(mWalletName)
                .setWalletLabel(mWalletLabel)
                .setWalletAmount(mWalletAmount);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING, getResources().getDisplayMetrics());
        setPadding(padding, padding, padding, padding);
        setOnClickListener(this);
    }

    public WalletView setWalletName(String walletName) {
        mWalletName = walletName;
        tvWalletName.setText(walletName);
        return this;
    }

    public WalletView setWalletAmount(String walletAmount) {
        mWalletAmount = walletAmount;
        tvWalletAmount.setText(walletAmount);
        return this;
    }

    public WalletView setWalletLabel(String walletLabel) {
        mWalletLabel = walletLabel;
        tvWalletLabel.setText(walletLabel);
        return this;
    }

    public WalletView setBackuped(boolean backuped) {
        this.backuped = backuped;
        refreshLayout();
        return this;
    }
    private void refreshLayout() {
        if (!backuped) {
            btnBackupNow.setVisibility(VISIBLE);
        } else {
            btnBackupNow.setVisibility(GONE);
        }
    }


    @OnClick(R.id.btn_backup_now)
    public void onViewClicked() {
        if (mOnWalletClickListener != null) {
            mOnWalletClickListener.onBackupClick(getWallet(), btnBackupNow);
        }

    }

    public Wallet getWallet() {
        return mWallet;
    }

    public WalletView setWallet(Wallet wallet) {
        mWallet = wallet;
        //填充布局数据
        this.setWalletName(StringUtils.cutWalletName(wallet.getName()))
                .setWalletLabel(String.valueOf(wallet.getName().charAt(0)))
                // TODO: 2017/11/28 从后台获余额
                .setWalletAmount(StringUtils.satoshi2btc(wallet.getBalance()) + " btc")
                .setBackuped(wallet.getIsBackuped());
        return this;
    }

    public WalletView setOnWalletClickListener(OnWalletClickListener onWalletClickListener) {
        mOnWalletClickListener = onWalletClickListener;
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

    public interface OnBackupClickListener {

    }

    public interface OnWalletClickListener {

        void onWalletClick(Wallet wallet, View view);

        void onBackupClick(Wallet wallet, View view);
    }
}
