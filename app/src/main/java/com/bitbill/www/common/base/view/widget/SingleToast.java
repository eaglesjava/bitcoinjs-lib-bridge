package com.bitbill.www.common.base.view.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bitbill.www.R;
import com.bitbill.www.common.utils.StringUtils;

import java.lang.ref.WeakReference;

/**
 * <pre>
 * SingleToast单例的吐司
 * Created by zhuyuanbao on 2016/3/21.
 * </pre>
 */
public class SingleToast {
    private static final Object SYNC_LOCK = new Object();
    /**
     * 上下文弱引用
     */
    public static WeakReference<Context> mContextWeakRef;
    private static Toast mToast;
    private static TextView msgTextView;

    public static Context getContext() {
        return mContextWeakRef.get();
    }

    public static void setContext(Context context) {
        SingleToast.mContextWeakRef = new WeakReference<Context>(context);
    }

    /**
     * 获取toast环境，为toast加锁
     *
     * @return
     */
    private static void init() {

        if (mToast == null) {
            synchronized (SYNC_LOCK) {
                if (mToast == null) {
                    Context context = mContextWeakRef.get();
                    mToast = new Toast(context);
                    LayoutInflater inflate = (LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflate.inflate(R.layout.layout_toast, null);
                    msgTextView = (TextView) v.findViewById(R.id.message);
                    mToast.setView(msgTextView);
                    float yOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
                    mToast.setGravity(Gravity.BOTTOM, 0, (int) yOffset);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
            }
        }
    }

    /**
     * 展示吐司
     *
     * @param context 环境
     * @param text    内容
     */
    public static void show(String text, Context context) {
        setContext(context);
        if (getContext() != null && StringUtils.isNotEmpty(text)) {
            init();
            msgTextView.setText(text);
            mToast.show();
        }
    }

    /**
     * 清除上下文引用
     */
    public static void clear() {
        if (mContextWeakRef != null) {
            mContextWeakRef.clear();
        }
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
