package com.bitbill.www.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liyanxi on 16/6/24.
 * 拦截系统ViewPager bug: java.lang.IllegalArgumentException: pointerIndex out of range
 */
public class CatchExceptionViewPager extends ViewPager {
    public CatchExceptionViewPager(Context context) {
        super(context);
    }

    public CatchExceptionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
