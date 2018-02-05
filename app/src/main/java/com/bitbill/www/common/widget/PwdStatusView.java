package com.bitbill.www.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.bitbill.www.R;


/**
 * 密码强度状态控件
 */
public class PwdStatusView extends View {
    public static final int DEFAULT_LEVEL = 0;
    public static final int DANGEROUS_LEVEL = 1;
    public static final int WEAK_LEVEL = 2;
    public static final int STRONG_LEVEL = 3;
    private static final int DEFAULT_COLOR_ID = R.color.white;
    private static final int DANGEROUS_COLOR_ID = R.color.red_weak;
    private static final int WEAK_COLOR_ID = R.color.yellow_normal;
    private static final int STRONG_COLOR_ID = R.color.green_strong;
    private StrongLevel mStrongLevel = StrongLevel.DEFAULT;
    private float mStatusItemHeight = 0;
    private float mStatusItemWidth = 0;
    private float mStatusItemGap = 0;
    private TextPaint mDefaultTextPaint;
    private TextPaint mLevelTextPaint;
    private int mDefaultColor;
    private int mStrongColor;
    private int mDangerousColor;
    private int mWeakColor;

    public PwdStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public PwdStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PwdStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PwdStatusView, defStyle, 0);

        mDefaultColor = a.getColor(
                R.styleable.PwdStatusView_defaultColor, getResources().getColor(DEFAULT_COLOR_ID));
        mDangerousColor = a.getColor(
                R.styleable.PwdStatusView_dangerousColor, getResources().getColor(DANGEROUS_COLOR_ID));
        mWeakColor = a.getColor(
                R.styleable.PwdStatusView_weakColor, getResources().getColor(WEAK_COLOR_ID));
        mStrongColor = a.getColor(
                R.styleable.PwdStatusView_strongColor,
                getResources().getColor(STRONG_COLOR_ID));
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mStatusItemWidth = a.getDimension(
                R.styleable.PwdStatusView_statusItemWidth,
                mStatusItemWidth);
        mStatusItemHeight = a.getDimension(
                R.styleable.PwdStatusView_statusItemHeight,
                mStatusItemHeight);
        mStatusItemGap = a.getDimension(
                R.styleable.PwdStatusView_statusItemGap,
                mStatusItemGap);


        a.recycle();

        // Set up a default TextPaint object
        mDefaultTextPaint = new TextPaint();
        mDefaultTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mDefaultTextPaint.setColor(mDefaultColor);

        // Set up a strong TextPaint object
        mLevelTextPaint = new TextPaint();
        mLevelTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLevelTextPaint.setColor(mDangerousColor);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        if (mStatusItemGap == 0 || mStatusItemWidth == 0 || mStatusItemHeight == 0) {

            mStatusItemGap = contentWidth / 20;
            mStatusItemWidth = mStatusItemGap * 6;
            mStatusItemHeight = contentHeight;

        }
        mDefaultTextPaint.setTextSize(mStatusItemHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawItem(0, canvas);
        drawItem(1, canvas);
        drawItem(2, canvas);
    }

    @NonNull
    private void drawItem(int index, Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = (mStatusItemWidth + mStatusItemGap) * index;
        rectF.top = 0;
        rectF.right = mStatusItemWidth * (index + 1) + mStatusItemGap * index;
        rectF.bottom = mStatusItemHeight;
        // Draw round rect.

        switch (mStrongLevel) {
            case DEFAULT:
                mLevelTextPaint.setColor(mDefaultColor);
                break;
            case DANGEROUS:
                mLevelTextPaint.setColor(mDangerousColor);
                break;
            case WEAK:
                mLevelTextPaint.setColor(mWeakColor);
                break;
            case STRONG:
                mLevelTextPaint.setColor(mStrongColor);
                break;
        }
        TextPaint textPaint = (mStrongLevel != null && (mStrongLevel.getLevel() > index))
                ? mLevelTextPaint : mDefaultTextPaint;
        canvas.drawRoundRect(rectF, 10, 10, textPaint);
    }

    public void setStrongLevel(StrongLevel level) {
        mStrongLevel = level;
        postInvalidate();
    }

    /**
     * 密码强度枚举
     */
    public enum StrongLevel {
        DEFAULT(DEFAULT_LEVEL),
        DANGEROUS(DANGEROUS_LEVEL),
        WEAK(WEAK_LEVEL),
        STRONG(STRONG_LEVEL);

        private final int mLevel;

        StrongLevel(int level) {
            this.mLevel = level;
        }

        public int getLevel() {
            return mLevel;
        }
    }
}
