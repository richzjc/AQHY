package com.micker.first.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class FirstStageView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {

    var jieShu : Int = 0
    var findWord : String = ""
    var proguardWord : String = ""


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

}