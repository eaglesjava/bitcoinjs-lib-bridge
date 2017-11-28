package com.bitbill.www.common.base.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bitbill.www.R;

import butterknife.BindView;

/**
 * Created by isanwenyu@163.com on 2017/11/28.
 */
public class PopupWalletMenu extends PopupWindow implements View.OnClickListener {
    @BindView(R.id.tv_menu_create_wallet)
    TextView tvMenuCreateWallet;
    @BindView(R.id.tv_menu_import_wallet)
    TextView tvMenuImportWallet;
    private View mWalletView;
    private OnWalletMenuItemClickListener mOnWalletMenuItemClickListener;
    private Context mContext;

    /**
     * <p>Create a new empty, non focusable popup window of dimension (0,0).</p>
     * <p>
     * <p>The popup does provide a background.</p>
     *
     * @param context
     */
    public PopupWalletMenu(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWalletView = inflater.inflate(R.layout.popup_wallet_menu, null);
        mWalletView.findViewById(R.id.tv_menu_create_wallet).setOnClickListener(this);
        mWalletView.findViewById(R.id.tv_menu_import_wallet).setOnClickListener(this);
        //设置PopupWalletMenu的View
        this.setContentView(mWalletView);
        //设置PopupWalletMenu弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWalletMenu弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public PopupWalletMenu setOnWalletMenuItemClickListener(OnWalletMenuItemClickListener onWalletMenuItemClickListener) {
        mOnWalletMenuItemClickListener = onWalletMenuItemClickListener;
        return this;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_menu_create_wallet:
                if (mOnWalletMenuItemClickListener != null) {
                    mOnWalletMenuItemClickListener.onCreateWallet(view);
                }
                break;
            case R.id.tv_menu_import_wallet:
                if (mOnWalletMenuItemClickListener != null) {
                    mOnWalletMenuItemClickListener.onImportWallet(view);
                }
                break;
        }
    }

    public void show(View v) {
        //获取PopupWindow中View的宽高
        mWalletView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredWidth = mWalletView.getMeasuredWidth();
        int measuredHeight = mWalletView.getMeasuredHeight();
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
        //显示在正上方
        super.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - measuredWidth, location[1] - measuredHeight - padding);


    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface OnWalletMenuItemClickListener {

        void onCreateWallet(View view);

        void onImportWallet(View view);
    }
}
