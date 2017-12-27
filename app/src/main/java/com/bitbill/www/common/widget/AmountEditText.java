package com.bitbill.www.common.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.bitbill.www.common.utils.StringUtils;

/**
 * Created by isanwenyu@163.com on 2017/12/14.
 */
public class AmountEditText extends android.support.v7.widget.AppCompatEditText {

    /**
     * 输入框小数的位数
     */
    private static final int DECIMAL_DIGITS = 8;

    private InputFilter lengthFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // source:当前输入的字符
            // start:输入字符的开始位置
            // end:输入字符的结束位置
            // dest：当前已显示的内容
            // dstart:当前光标开始位置
            // dent:当前光标结束位置
            if (dest.length() == 0 && source.equals(".")) {
                return "0.";
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                if (dotValue.length() == DECIMAL_DIGITS) {
                    return "";
                }
            }
            return null;
        }

    };

    public AmountEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public AmountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AmountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        setFilters(new InputFilter[]{lengthFilter});
        StringUtils.setAmountTypeface(getContext(), this);
    }

}
