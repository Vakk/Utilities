package com.gabbb.gabbb.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.LinearLayout
import com.gabbb.gabbb.R
import com.gabbb.gabbb.util.TextUtils
import kotlinx.android.synthetic.main.view_checkable_control.view.*

/**
 * Created by Valery Kotsulym on 2/8/18.
 */

class CheckableCounterView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defaultResId: Int = 0
) : LinearLayout(context, attrs, defaultResId) {

    var value: Long = 0
        set(counterValue) {
            field = counterValue
            rebindValue()
        }

    var animateEnable: Boolean = false

    var showZeroValue: Boolean = false
        set(showZeroValue) {
            field = showZeroValue
            rebindValue()
        }

    var ellipsizeCounter: Boolean = false
        set(needEllipsizeValue) {
            field = needEllipsizeValue
            rebindValue()
        }

    private var isReady = false

    private var currentAnimation: ViewPropertyAnimator? = null

    init {
        View.inflate(context, R.layout.view_checkable_control, this)

        val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CheckableCounterView,
                defaultResId,
                0
        )

        try {
            typedArray.let {

                setChecked(it.getBoolean(R.styleable.CheckableCounterView_checked, false))

                ellipsizeCounter = it.getBoolean(R.styleable.CheckableCounterView_ellipsizeValue,
                        true)
                showZeroValue = it.getBoolean(R.styleable.CheckableCounterView_showZeroValue,
                        true)
                animateEnable = it.getBoolean(R.styleable.CheckableCounterView_animateEnable,
                        true)

                value = it.getInteger(R.styleable.CheckableCounterView_value, 0)
                        .toLong()
                val iconId = it.getResourceId(
                        R.styleable.CheckableCounterView_icon,
                        R.drawable.ic_like_black_24dp
                )
                civImage?.setImageResource(iconId)
            }

        } finally {
            typedArray?.recycle()
        }
        isReady = true
        rebindValue()
    }

    private fun rebindValue() {
        if (isReady) {
            ctvText?.text =
                    if (ellipsizeCounter) TextUtils.formatCounter(context, value)
                    else "$value"

            ctvText?.visibility =
                    if (value == 0L && !showZeroValue) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
        }
    }

    fun setChecked(checked: Boolean) {
        ctvText?.isChecked = checked
        civImage?.isChecked = checked
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (animateEnable) {
            currentAnimation?.cancel()
            currentAnimation = animate().alpha(if (enabled) 1.0f else 0.5f).setDuration(500)
            currentAnimation?.start()
        }
    }


}
