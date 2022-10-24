package com.wallstreetcn.baseui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout

class LiveShareLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var view1: LiveShareIconView? = null
    var rectColor: Int = Color.BLACK
    var text : String? = ""
    set(value) {
        field = value
        view1?.setText(value)
    }

    init {
        setWillNotDraw(false)
        orientation = HORIZONTAL
        view1 = LiveShareIconView(getContext())
        addView(view1)
    }

    private val paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint
    }

    fun bindText(leftFontId : Int, rightFontId : Int, leftText : String, rightText : String){
        rectColor = Color.parseColor("#1478f0")
//        view2?.rectColor = Color.parseColor("#59000000")
//        view1?.fontId = leftFontId
//        view2?.fontId = rightFontId
        view1?.text = leftText
//        view2?.text = rightText
    }

//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        super.onLayout(changed, l, t, r, b)
//        if(view1 != null && view2 != null){
//            val view1Width = view1!!.measuredWidth
//            val view1Height = view1!!.measuredHeight
//
//            view1?.layout(0, 0, view1Width, view1Height)
//
//            view2?.layout((view1Width - view1!!.gap).toInt(), 0, (view1Width - view1!!.gap).toInt() + view2!!.measuredWidth, view2!!.measuredHeight)
//        }
//    }


    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)

        val gap = Math.tan(Math.PI * 12.0 / 180) * measuredHeight
        paint.color = rectColor
        val path = Path()

        var startX = gap.toFloat()
        var startY = 0f
        path.moveTo(startX, startY)

        startX = measuredWidth.toFloat()
        startY = 0f

        path.lineTo(startX, startY)

        startX = measuredWidth.toFloat() - gap.toFloat()
        startY = measuredHeight.toFloat()

        path.lineTo(startX, startY)

        startX = 0f
        startY = measuredHeight.toFloat()

        path.lineTo(startX, startY)

        canvas?.drawPath(path, paint)


    }

}