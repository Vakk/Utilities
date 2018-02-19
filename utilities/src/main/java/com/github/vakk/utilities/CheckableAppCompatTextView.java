package com.gabbb.gabbb.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.gabbb.gabbb.R;

public class CheckableAppCompatTextView extends AppCompatTextView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private ColorStateList mCheckedColor;
    private ColorStateList mUnCheckedColor;

    private boolean mChecked = false;

    public CheckableAppCompatTextView(Context context) {
        this(context, null);
    }

    public CheckableAppCompatTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableAppCompatTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckableAppCompatImageView, defStyleAttr, 0);

        try {
            mChecked = a.getBoolean(R.styleable.CheckableAppCompatImageView_checked, false);
            mCheckedColor = a.getColorStateList(R.styleable.CheckableAppCompatImageView_checkedColor);
            mUnCheckedColor = a.getColorStateList(R.styleable.CheckableAppCompatImageView_uncheckedColor);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        ColorStateList colorState = mChecked ? mCheckedColor : mUnCheckedColor;
        if (colorState != null) {
            if (colorState.isStateful()) {
                int color = colorState.getColorForState(getDrawableState(), 0);
                setTextColor(color);
            } else {
                int color = colorState.getDefaultColor();
                setTextColor(color);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
//        View.OnClickListener onClickListener = v -> {
//            toggle();
//            l.onClick(v);
//        };
        super.setOnClickListener(l);
    }
}