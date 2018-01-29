package com.bitbill.www.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * <p>Detects Keyboard Status changes and fires events only once for each change.</p>
 */
public class KeyboardStatusDetector {
    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 200;
    private static final String TAG = "KeyboardStatusDetector";
    boolean keyboardVisible = false;
    private KeyboardVisibilityListener mVisibilityListener;
    private OnGlobalLayoutListener mOnGlobalLayoutListener;
    private int rootViewVisibleHeight;

    public void registerFragment(Fragment f) {
        registerView(f.getView());
    }

    public void unRegisterFragment(Fragment f) {
        unRegisterView(f.getView());
    }

    public void registerActivity(Activity a) {
        registerView(a.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public void unRegisterActivity(Activity a) {
        unRegisterView(a.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public KeyboardStatusDetector registerView(final View v) {
        if (mOnGlobalLayoutListener == null) {
            mOnGlobalLayoutListener = new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    v.getWindowVisibleDisplayFrame(r);
                    int visibleHeight = r.height();
                    if (rootViewVisibleHeight == 0) {
                        rootViewVisibleHeight = visibleHeight;
                        return;
                    }
                    //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                    if (rootViewVisibleHeight == visibleHeight) {
                        return;
                    }
                    int heightDiff = v.getRootView().getHeight() - visibleHeight;
                    Log.d(TAG, "onGlobalLayout() called visibleHeight:" + visibleHeight + ",heightDiff:" + heightDiff);
                    if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) { // if more than 200 pixels, its probably a keyboard...
                        if (!keyboardVisible) {
                            keyboardVisible = true;
                            if (mVisibilityListener != null) {
                                mVisibilityListener.onVisibilityChanged(true);
                            }
                        }
                    } else {
                        if (keyboardVisible) {
                            keyboardVisible = false;
                            if (mVisibilityListener != null) {
                                mVisibilityListener.onVisibilityChanged(false);
                            }
                        }
                    }
                    rootViewVisibleHeight = visibleHeight;
                }
            };
            v.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

        return this;
    }

    public KeyboardStatusDetector unRegisterView(final View view) {
        if (mOnGlobalLayoutListener != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);

        }
        return this;
    }

    public KeyboardStatusDetector setKeyboardVisibilityListener(KeyboardVisibilityListener listener) {
        mVisibilityListener = listener;
        return this;
    }

    public interface KeyboardVisibilityListener {
        void onVisibilityChanged(boolean keyboardVisible);
    }

}