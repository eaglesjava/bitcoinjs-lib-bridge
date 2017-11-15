package com.bitbill.www.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitbill.www.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 输入框包装类
 */
public class EditTextWapper extends LinearLayout {
    public static final int INPUT_PWD_VISIBLE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;//密码显示输入方式
    public static final int INPUT_PWD_HIDE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;//密码隐藏输入方式
    //input type constants
    private static final int INPUT_DEFAULT = InputType.TYPE_CLASS_TEXT;//默认输入方式
    private static final int INPUT_NUMBER = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;//数字输入方式
    private static final int NOARMAL_TEXT_COLOR = R.color.white;
    private static final int ERROR_TEXT_COLOR = R.color.error;
    private static SparseArray<Integer> INPUT_TYPE_ARRAY = new SparseArray<>();

    static {
        INPUT_TYPE_ARRAY.put(0, INPUT_DEFAULT);
        INPUT_TYPE_ARRAY.put(1, INPUT_PWD_VISIBLE);
        INPUT_TYPE_ARRAY.put(2, INPUT_PWD_HIDE);
        INPUT_TYPE_ARRAY.put(3, INPUT_NUMBER);
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_text)
    EditText etText;
    private String mInputTitle;
    private String mInputHint;
    private float mInputPadding = 10;
    private Drawable mRightDrawable;
    private int mInputType = INPUT_DEFAULT;
    private int mMaxLines = 1;
    private float mInputPaddingBottom = mInputPadding;
    private float mInputPaddingRight = mInputPadding;
    private float mInputPaddingTop = mInputPadding;
    private float mInputPaddingLeft = mInputPadding;

    public EditTextWapper(Context context) {
        super(context);
        init(null, 0);
    }

    public EditTextWapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EditTextWapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.EditTextWapper, defStyle, 0);

        mInputTitle = a.getString(
                R.styleable.EditTextWapper_inputTitle);
        mInputHint = a.getString(
                R.styleable.EditTextWapper_inputHint);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mInputPadding = a.getDimension(
                R.styleable.EditTextWapper_inputPadding,
                mInputPadding);
        mInputPaddingLeft = a.getDimension(
                R.styleable.EditTextWapper_inputPaddingLeft,
                mInputPadding);
        mInputPaddingTop = a.getDimension(
                R.styleable.EditTextWapper_inputPaddingTop,
                mInputPadding);
        mInputPaddingRight = a.getDimension(
                R.styleable.EditTextWapper_inputPaddingRight,
                mInputPadding);
        mInputPaddingBottom = a.getDimension(
                R.styleable.EditTextWapper_inputPaddingBottom,
                mInputPadding);
        mMaxLines = a.getInt(
                R.styleable.EditTextWapper_maxLines,
                mMaxLines);
        mInputType = a.getInt(
                R.styleable.EditTextWapper_inputType,
                mInputType);
        if (a.hasValue(R.styleable.EditTextWapper_rightDrawable)) {
            mRightDrawable = a.getDrawable(
                    R.styleable.EditTextWapper_rightDrawable);
            mRightDrawable.setCallback(this);
        }
        a.recycle();

        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_edit_text_wapper, this);
        ButterKnife.bind(this);
        setOrientation(VERTICAL);
        setHintTitle(mInputTitle);
        setInputHint(mInputHint);
        setInputPadding();
        setInputType(mInputType);
        setMaxLines(mMaxLines);
    }

    private void setInputHint(String inputHint) {
        mInputHint = inputHint;
        getEtText().setHint(inputHint);
    }

    @NonNull
    private EditText getEtText() {
        return etText;
    }

    private void setHintTitle(String inputTitle) {
        mInputTitle = inputTitle;
        getTvTitle().setText(inputTitle);
    }

    @NonNull
    private TextView getTvTitle() {
        return tvTitle;
    }

    public void setInputPadding() {
        getEtText().setPadding(((int) mInputPaddingLeft), ((int) mInputPaddingTop), ((int) mInputPaddingRight), ((int) mInputPaddingBottom));
    }

    public void setInputType(int inputType) {
        this.mInputType = inputType;
        getEtText().setInputType(INPUT_TYPE_ARRAY.get(inputType));
    }

    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
    }

    @SuppressLint("ResourceAsColor")
    public void setError(String error) {
        getTvTitle().setText(error);
        getTvTitle().setTextColor(ERROR_TEXT_COLOR);
    }

    @SuppressLint("ResourceAsColor")
    public void removeError() {
        getTvTitle().setTextColor(NOARMAL_TEXT_COLOR);
    }

    public CharSequence getText() {
        return getEtText().getText();
    }
}
