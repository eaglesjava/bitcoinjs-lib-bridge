package com.bitbill.www.common.base.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bitbill.www.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 输入框包装类
 */
public class EditTextWapper extends FrameLayout {
    public static final int INPUT_PWD_VISIBLE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;//密码显示输入方式
    public static final int INPUT_PWD_HIDE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;//密码隐藏输入方式
    //input type constants
    private static final int INPUT_DEFAULT = InputType.TYPE_CLASS_TEXT;//默认输入方式
    private static final int INPUT_NUMBER = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;//数字输入方式
    private static final int NOARMAL_TEXT_COLOR = R.color.white;
    private static final int ERROR_TEXT_COLOR = R.color.error;
    private static final int EDIT_LINE_COLOR = R.color.edit_line;
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
    @BindView(R.id.psv_status)
    PwdStatusView psvStatus;
    @BindView(R.id.tv_etw_bottom_hint)
    TextView bottomHintTextView;
    @BindView(R.id.et_line)
    View lineView;

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
    private boolean mInputPwdStatusVisible = false;
    private boolean mErrorState;
    private TextWatcher mTextWatcher;
    private String mBottomHint;

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
        mInputPwdStatusVisible = a.getBoolean(
                R.styleable.EditTextWapper_inputPwdStatusVisible,
                mInputPwdStatusVisible);
        if (a.hasValue(R.styleable.EditTextWapper_rightDrawable)) {
            mRightDrawable = a.getDrawable(
                    R.styleable.EditTextWapper_rightDrawable);
            mRightDrawable.setCallback(this);
        }
        mBottomHint = a.getString(
                R.styleable.EditTextWapper_bottomHint);
        a.recycle();

        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_edit_text_wapper, this);
        ButterKnife.bind(this);
        setHintTitle(mInputTitle);
        setInputHint(mInputHint);
        setInputPadding();
        setInputType(mInputType);
        setMaxLines(mMaxLines);
        setInputPwdStatusVisible(mInputPwdStatusVisible);

        getEtText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isError()) {
                    removeError();
                }
                if (mTextWatcher != null) {
                    mTextWatcher.beforeTextChanged(s, start, count, after);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTextWatcher != null) {
                    mTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTextWatcher != null) {
                    mTextWatcher.afterTextChanged(s);
                }
            }
        });

        setBottomHint(mBottomHint);

        getEtText().setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(mBottomHint)) {
                    //弹出底部提示
                    bottomHintTextView.setVisibility(VISIBLE);
                } else {
                    bottomHintTextView.setVisibility(GONE);
                }

                if (isError()) {
                    lineView.setBackgroundColor(getResources().getColor(ERROR_TEXT_COLOR));
                } else if (hasFocus) {
                    lineView.setBackgroundColor(getResources().getColor(NOARMAL_TEXT_COLOR));
                } else {
                    lineView.setBackgroundColor(getResources().getColor(EDIT_LINE_COLOR));
                }
            }
        });
        getEtText().clearFocus();
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

    public void setError(String error) {
        if (TextUtils.isEmpty(error)) {
            removeError();
        } else {
            getTvTitle().setText(error);
            getTvTitle().setTextColor(getResources().getColor(ERROR_TEXT_COLOR));
        }
    }

    public void removeError() {
        getTvTitle().setTextColor(getResources().getColor(NOARMAL_TEXT_COLOR));
        getTvTitle().setText(mInputTitle);
        setErrorState(false);
        lineView.setBackgroundColor(getResources().getColor(EDIT_LINE_COLOR));
    }

    public void setErrorState(boolean errorState) {
        this.mErrorState = errorState;
    }

    public boolean isError() {
        return mErrorState;
    }

    public void setError(@StringRes int errorStringId) {
        if (errorStringId == 0) {
            removeError();
        } else {
            getTvTitle().setText(errorStringId);
            getTvTitle().setTextColor(getResources().getColor(ERROR_TEXT_COLOR));
            setErrorState(true);
        }

    }

    public String getText() {
        return getEtText().getText().toString();
    }

    public void setInputPwdStatusVisible(boolean inputPwdStatusVisible) {
        mInputPwdStatusVisible = inputPwdStatusVisible;
        psvStatus.setVisibility(inputPwdStatusVisible ? View.VISIBLE : View.GONE);
    }

    public void addInputTextWatcher(TextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
    }

    public void setStrongLevel(PwdStatusView.StrongLevel strongLevel) {
        if (getPsvStatusView() == null) {
            return;
        }
        getPsvStatusView().setStrongLevel(strongLevel);
    }

    public PwdStatusView getPsvStatusView() {
        return psvStatus;
    }

    public EditTextWapper setBottomHint(String bottomHint) {
        mBottomHint = bottomHint;
        bottomHintTextView.setText(mBottomHint);
        return this;
    }
}
