package com.gabbb.gabbb.view

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import com.gabbb.gabbb.R

class CheckableAppCompatImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), Checkable {

    private var checkedColor: ColorStateList? = null
    private var unCheckedColor: ColorStateList? = null

    private var checked = false

    init {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CheckableAppCompatImageView,
                defStyleAttr, 0
        )

        try {

            checked = typedArray?.getBoolean(R.styleable.CheckableAppCompatImageView_checked, false)
                    ?: false

            checkedColor = typedArray?.getColorStateList(R.styleable.CheckableAppCompatImageView_checkedColor)
            unCheckedColor = typedArray?.getColorStateList(R.styleable.CheckableAppCompatImageView_uncheckedColor)
        } finally {
            typedArray?.recycle()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        val colorState = if (checked) checkedColor else unCheckedColor
        if (colorState != null) {
            if (colorState.isStateful) {
                val color = colorState.getColorForState(drawableState, 0)
                setColorFilter(color)
            } else {
                val color = colorState.defaultColor
                setColorFilter(color)
            }
        }
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked != checked) {
            this.checked = checked
            refreshDrawableState()
        }
    }

    override fun toggle() {
        isChecked = !checked
    }

    companion object {

        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}