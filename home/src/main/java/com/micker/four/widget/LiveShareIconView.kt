package com.wallstreetcn.baseui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.micker.helper.system.ScreenUtils

class LiveShareIconView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {


    private val setHeight = ScreenUtils.dip2px(22f)
    val gap by lazy {
        Math.tan(Math.PI * 6.0 / 180) * setHeight
    }

    private var customTypeFace: Typeface? = null


//    var fontId : Int = R.font.iconfont
//    set(value) {
//        field = value
//        initTypeFace(value)
//    }

    var rectColor: Int = Color.BLACK

    init {
        val gap = Math.tan(Math.PI * 6.0 / 180) * setHeight
        setTextColor(Color.WHITE)
        val topPadding = 0
        val leftPadding = ScreenUtils.dip2px(10f)
        setPadding(leftPadding + gap.toInt(), topPadding, leftPadding, topPadding)
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        maxLines = 1
        setText("测试")
    }

    private val paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint
    }

//    fun initTypeFace(value : Int) {
//        val gettypeFace = ResourcesCompat.getFont(context, value)
//        setCustomTypeFace(gettypeFace)
//    }


    fun setCustomTypeFace(customTypeFace: Typeface?) {
        this.customTypeFace = customTypeFace
        if (null != customTypeFace) setTypeface(customTypeFace)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, setHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.skew(-Math.tan(Math.PI * 6.0 / 180).toFloat(), 0f)
        paint.color = rectColor
        val gap = Math.tan(Math.PI * 6.0 / 180) * measuredHeight

//        canvas?.drawRect(
//            gap.toFloat(),
//            0f,
//            measuredWidth.toFloat(),
//            measuredHeight.toFloat(),
//            paint
//        )

        val path = Path()
        var startX = gap.toFloat() * 2
        var startY = 0f
        path.moveTo(startX, startY)

        startX = measuredWidth.toFloat()
        startY = 0f

        path.lineTo(startX, startY)

        startX = measuredWidth.toFloat()
        startY = measuredHeight.toFloat()

        path.lineTo(startX, startY)

        startX = 0f
        startY = measuredHeight.toFloat()

        path.lineTo(startX, startY)

        canvas?.drawPath(path, paint)
        super.onDraw(canvas)
        canvas?.restore()
    }
}