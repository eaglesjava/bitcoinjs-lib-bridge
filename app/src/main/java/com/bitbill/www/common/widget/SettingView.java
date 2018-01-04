package com.bitbill.www.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bitbill.www.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: document your custom view class.
 */
public class SettingView extends FrameLayout {
    private static final int RIGHT_TEXT_COLOR = R.color.white_60;
    @BindView(R.id.tv_left)
    TextView mTvLeft;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_right_arrow)
    ImageView mIvRightArrow;
    @BindView(R.id.switch_right)
    Switch mSwitchRight;
    @BindView(R.id.v_line)
    View mVLine;
    private String mLeftText;
    private String mRightText;
    private Drawable mRightDrawable;
    private boolean hasSwitch;
    private int mRightStringColor;
    private boolean hasBottomLine;


    public SettingView(Context context) {
        super(context);
        init(null, 0);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SettingView, defStyle, 0);

        mLeftText = a.getString(
                R.styleable.SettingView_leftString);
        mRightText = a.getString(
                R.styleable.SettingView_rightString);
        mRightStringColor = a.getColor(
                R.styleable.SettingView_rightStringColor, getResources().getColor(RIGHT_TEXT_COLOR));
        hasSwitch = a.getBoolean(
                R.styleable.SettingView_hasSwitch, hasSwitch);
        hasBottomLine = a.getBoolean(
                R.styleable.SettingView_hasBottomLine, hasBottomLine);
        if (a.hasValue(R.styleable.SettingView_rightResource)) {
            mRightDrawable = a.getDrawable(
                    R.styleable.SettingView_rightResource);
            mRightDrawable.setCallback(this);
        }

        a.recycle();
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_setting_view, this);
        ButterKnife.bind(this);
        setLeftText(mLeftText)
                .setRightText(mRightText)
                .setRightStringColor(mRightStringColor)
                .setRightDrawable(mRightDrawable)
                .setHasSwitch(hasSwitch)
                .setHasBottomLine(hasBottomLine);
    }

    public SettingView setLeftText(String leftText) {
        mLeftText = leftText;
        mTvLeft.setText(mLeftText);
        return this;
    }

    public SettingView setRightText(String rightText) {
        mRightText = rightText;
        mTvRight.setText(mRightText);
        return this;
    }

    public SettingView setRightDrawable(Drawable rightDrawable) {
        mRightDrawable = rightDrawable;
        mIvRightArrow.setImageDrawable(rightDrawable);
        return this;
    }

    public SettingView setRightStringColor(@ColorInt int rightStringColor) {
        mRightStringColor = rightStringColor;
        mTvRight.setTextColor(mRightStringColor);
        return this;
    }

    public SettingView setHasSwitch(boolean hasSwitch) {
        this.hasSwitch = hasSwitch;
        //refresh layout
        mTvRight.setVisibility(hasSwitch ? GONE : VISIBLE);
        mIvRightArrow.setVisibility(hasSwitch ? GONE : VISIBLE);
        mSwitchRight.setVisibility(hasSwitch ? VISIBLE : GONE);
        return this;
    }

    public SettingView setHasBottomLine(boolean hasBottomLine) {
        this.hasBottomLine = hasBottomLine;
        mVLine.setVisibility(hasBottomLine ? VISIBLE : GONE);
        return this;
    }

    public SettingView setOnRightSwitchCheckedChangeListener(CompoundButton.OnCheckedChangeListener onRightSwitchCheckedChangeListener) {
        mSwitchRight.setOnCheckedChangeListener(onRightSwitchCheckedChangeListener);
        return this;
    }

    public SettingView setSwitchChecked(boolean checked) {
        mSwitchRight.setChecked(checked);
        return this;
    }
}
