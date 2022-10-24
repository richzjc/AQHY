package com.wallstreetcn.baseui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout

class LiveShareLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var view1: LiveShareIconView? = null
    var view2: LiveShareIconView? = null

    init {
        orientation = HORIZONTAL
        view1 = LiveShareIconView(getContext())
        view2 = LiveShareIconView(getContext())
        addView(view1)
//        addView(view2)
    }

    fun bindText(leftFontId : Int, rightFontId : Int, leftText : String, rightText : String){
        view1?.rectColor = Color.parseColor("#1478f0")
        view2?.rectColor = Color.parseColor("#59000000")
//        view1?.fontId = leftFontId
//        view2?.fontId = rightFontId
        view1?.text = leftText
        view2?.text = rightText
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

}