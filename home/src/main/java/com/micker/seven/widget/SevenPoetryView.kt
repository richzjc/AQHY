package com.micker.seven.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.micker.helper.system.ScreenUtils

class SevenPoetryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var list: List<String>? = null
    init {
        orientation = VERTICAL
    }

    fun bindData(list: List<String>) {
        this.list = list
        removeAllViews()
        if(list != null){
            list.forEachIndexed {index, value ->
                val view = SevenPoetryRowView(context)
                val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if(index > 0)
                    params.topMargin = ScreenUtils.dip2px(15f)
                view.bindData(value)
                addView(view, params)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}