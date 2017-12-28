package com.bitbill.www.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by isanwenyu on 2017/12/28.
 */

public class DrawableEditText extends android.support.v7.widget.AppCompatEditText {

    private OnClickListener mOnRightDrawableClickListener;

    public DrawableEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    //里面写上自己想做的事情，也就是DrawableRight的触发事件
                    if (mOnRightDrawableClickListener != null) {
                        mOnRightDrawableClickListener.onClick(this);
                    }

                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnRightDrawableClickListener(OnClickListener onRightDrawableClickListener) {
        mOnRightDrawableClickListener = onRightDrawableClickListener;
    }
}
