package com.micker.home.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.core.widget.endless.EndlessRecyclerAdapter
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import java.lang.IllegalStateException

class HorizontalRecycleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mAdapter: EndlessRecyclerAdapter? = null
    private var flag: Boolean = false
    private var footerView: View? = null
    private var isInAnimation: Boolean = false
    private var desc: TextView? = null
    var callback: SwitchToNextCallback? = null

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        setItemAnimator(itemAnimator)
        addListener()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is BaseRecycleAdapter<*, *>) {
            mAdapter = EndlessRecyclerAdapter(adapter)
            addFooter()
            swapAdapter(mAdapter, true)
        } else {
            throw IllegalStateException("传入的Adapter 必须为BaseRecycleAdapter的子类")
        }
    }

    private fun addFooter() {
        footerView = LayoutInflater.from(context).inflate(R.layout.aqhy_view_horizontal_rv_footer, this, false)
        desc = footerView?.findViewById(R.id.desc)
        footerView?.layoutParams = StaggeredGridLayoutManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val params = desc?.layoutParams as? RelativeLayout.LayoutParams
        params?.leftMargin = ScreenUtils.dip2px(15f)
        params?.rightMargin = ScreenUtils.dip2px(15f)
        desc?.layoutParams = params
        mAdapter?.addFooterView(footerView)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        val flag = super.onTouchEvent(e)
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                desc?.text = "滑动 . 查看全部"
            }
            MotionEvent.ACTION_MOVE -> {
                responseActionMove(e)
            }
            else -> {
                checkToSwitch()
                responseActionOther()
            }
        }
        return flag
    }

    private fun checkToSwitch(){
        val manager = layoutManager as LinearLayoutManager
        manager?.apply {
            val lastItem = findLastCompletelyVisibleItemPosition()
            Log.i("move", "$itemCount  $lastItem")
            if (lastItem == itemCount - 1) {
               callback?.swtichToNext()
            }
        }
    }

    private fun responseActionMove(e: MotionEvent) {
        val manager = layoutManager as LinearLayoutManager
        manager?.apply {
            val lastItem = findLastCompletelyVisibleItemPosition()
            Log.i("move", "$itemCount  $lastItem")
            if (lastItem == itemCount - 1) {
                desc?.text = "松开 . 查看全部"
            }
        }
    }

    private fun responseActionOther() {
        Log.i("onTouch", "up")
        flag = false
        val manager = layoutManager as LinearLayoutManager
        manager?.apply {
            val lastItem = findLastVisibleItemPosition()
            if (lastItem == (itemCount - 1)) {
                val offsetX = footerView!!.width - (footerView!!.right - ScreenUtils.getScreenWidth())
                animationClose(offsetX)
            }
        }
    }

    private fun animationClose(offsetX: Int) {
        if (!isInAnimation) {
            var lastValue = 0
            val animator = ValueAnimator.ofInt(0, offsetX)
            animator.addUpdateListener {
                val value = it.animatedValue as Int
                scrollBy(-(value - lastValue), 0)
                lastValue = value
                Log.i("animation", "$offsetX  $value")
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    isInAnimation = false
                    desc?.text = "滑动 . 查看全部"
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {
                    isInAnimation = true
                }

            })
            animator.duration = 300
            animator.start()
        }
    }

    private fun addListener() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    responseActionOther()
                }
            }
        })
    }

    interface SwitchToNextCallback {
        fun swtichToNext()
    }
}