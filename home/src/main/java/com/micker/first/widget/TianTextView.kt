package com.micker.first.widget

import android.content.Context
import android.graphics.*
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
        paint?.color = ResourceUtils.getColor(R.color.day_mode_text_color2_666666)

        virtualPaint = Paint()
        virtualPaint?.isAntiAlias = true
        virtualPaint?.setColor(Color.parseColor("#50999999"))
        virtualPaint?.setStyle(Paint.Style.STROKE)
        gravity = Gravity.CENTER
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val screenWidth = ScreenUtils.getScreenWidth()
        val itemWidth = screenWidth / 8
        val width = max(ScreenUtils.dip2px(30f).toFloat(), itemWidth * 0.8f).toInt()
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

        val mPath = Path()
        mPath.moveTo(0f, 0f)
        mPath.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
        val effects: PathEffect = DashPathEffect(floatArrayOf(20f, 10f), 1.0f)
        virtualPaint?.pathEffect = effects
        canvas!!.drawPath(mPath, virtualPaint!!)



        val mPath1 = Path()
        mPath1.moveTo(measuredWidth.toFloat(), 0f)
        mPath1.lineTo(0f, measuredHeight.toFloat())
        canvas!!.drawPath(mPath1, virtualPaint!!)


        val mPath2 = Path()
        mPath2.moveTo(measuredWidth.toFloat()/2, 0f)
        mPath2.lineTo(measuredWidth.toFloat()/2, measuredHeight.toFloat())
        canvas!!.drawPath(mPath2, virtualPaint!!)



        val mPath3 = Path()
        mPath3.moveTo(0f, measuredHeight.toFloat()/2)
        mPath3.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat()/2)
        canvas!!.drawPath(mPath3, virtualPaint!!)

        super.onDraw(canvas)
    }
}