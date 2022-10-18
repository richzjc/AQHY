package com.micker.home.widget.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.micker.helper.system.ScreenUtils
import com.micker.home.R

class ColorListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var recycler: Recycler? = null
    private var needRelayout: Boolean = true
    var ballWidth: Int = 0
    private val verticalPadding = ScreenUtils.dip2px(5f)
    private var horizontalPadding = 0

    var adapter: Adapter? = null
        set(value) {
            field = value
            recycler = Recycler()
            needRelayout = true
            requestLayout()
        }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (needRelayout || changed) {
            needRelayout = false
            removeAllViews()
            if (adapter != null) {
                var leftX = ballWidth
                var leftTop = verticalPadding
                (0 until adapter!!.getItemCount())?.forEach {
                    val view = obtainView(it)
                    view.layout(leftX, leftTop, leftX + ballWidth, leftTop + ballWidth)
                    if (it < 6) {
                        leftX += (ballWidth + horizontalPadding)
                    } else if (it == 6) {
                        leftTop = 2 * verticalPadding + ballWidth
                        leftX = ballWidth + (ballWidth / 2) + (horizontalPadding / 2)
                    } else {
                        leftX += (ballWidth + horizontalPadding)
                    }
                }
            }
        }
    }

    private fun obtainView(position: Int): View {
        val itemType = adapter!!.getItemViewType(position)
        var viewFromRecycler = recycler?.get(itemType)
        var view: View? = null
        if (viewFromRecycler == null) {
            view = adapter!!.onCreateViewHodler(position, viewFromRecycler, this)
            if (view == null) {
                throw RuntimeException("onCreateViewHodler  必须填充布局")
            }
        } else {
            view = viewFromRecycler
        }
        view.setTag(R.id.tag_type_view, itemType)
        adapter!!.onBinderViewHodler(position, view, this)
        view.measure(
            MeasureSpec.makeMeasureSpec(ballWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(ballWidth, MeasureSpec.EXACTLY)
        )
        val params = ViewGroup.LayoutParams(ballWidth, ballWidth)
        addView(view, params)
        return view!!
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        ballWidth = (height - 3 * verticalPadding) / 2
        horizontalPadding = (width - 2 * ballWidth - 7 * ballWidth) / 6
        if(horizontalPadding < ScreenUtils.dip2px(10f)){
            horizontalPadding = ScreenUtils.dip2px(10f)
            ballWidth = (width - 6 * horizontalPadding)/9
        }
    }

    override fun removeView(view: View?) {
        super.removeView(view)
        val key = view?.getTag(R.id.tag_type_view) as Int
        recycler?.put(view, key)
    }
}