package com.micker.first.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.micker.helper.ResourceUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R

class TianTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var paint: Paint? = null
    private var virtualPaint : Paint? = null
    init {
        paint = Paint()
        paint?.isAntiAlias = true
        paint?.strokeWidth = ScreenUtils.dip2px(1f).toFloat()
        paint?.color = ResourceUtils.getColor(R.color.day_mode_text_color1_333333)

        virtualPaint = Paint()
        virtualPaint?.isAntiAlias = true

    }

    override fun onDraw(canvas: Canvas?) {
//        paint?.strokeWidth = ScreenUtils.dip2px(1f).toFloat()
//        paint?.color = ResourceUtils.getColor(R.color.day_mode_text_color1_333333)
//        val rect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
//        canvas?.drawRect(rect, paint!!)

        super.onDraw(canvas)
    }
}