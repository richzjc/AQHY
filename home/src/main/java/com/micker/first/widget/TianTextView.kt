package com.micker.first.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.micker.helper.ResourceUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlin.math.max

class TianTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var paint: Paint? = null
    private var virtualPaint: Paint? = null

    init {
        paint = Paint()
        paint?.isAntiAlias = true
        paint?.strokeWidth = ScreenUtils.dip2px(1f).toFloat()
        paint?.color = ResourceUtils.getColor(R.color.day_mode_text_color1_333333)

        virtualPaint = Paint()
        virtualPaint?.isAntiAlias = true
        gravity = Gravity.CENTER
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val screenWidth = ScreenUtils.getScreenWidth()
        val itemWidth = screenWidth / 8
        val width = max(ScreenUtils.dip2px(30f).toFloat(), itemWidth * 2 / 3f).toInt()
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawLine(0f, 0f, measuredWidth.toFloat(), 0f, paint!!)
        canvas?.drawLine(0f, 0f, 0f, measuredHeight.toFloat(), paint!!)
        canvas?.drawLine(
            measuredWidth.toFloat(),
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            paint!!
        )
        canvas?.drawLine(
            0f,
            measuredHeight.toFloat(),
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            paint!!
        )
        super.onDraw(canvas)
    }
}