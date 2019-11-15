package com.priamm.citymobile

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class Car : AppCompatImageView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setImageResource(R.drawable.car)
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val measuredWidth = (32 * Resources.getSystem().displayMetrics.density).toInt()
        val measuredHeight = (64 * Resources.getSystem().displayMetrics.density).toInt()

        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(
                        measuredWidth,
                        MeasureSpec.EXACTLY
                ),
                MeasureSpec.makeMeasureSpec(
                        measuredHeight,
                        MeasureSpec.EXACTLY)
        )
    }
}