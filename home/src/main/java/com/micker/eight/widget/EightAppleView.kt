package com.micker.eight.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.WscnImageView
import com.micker.helper.ResourceUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.wallstreetcn.rpc.coroutines.requestData
import kotlinx.coroutines.delay
import kotlin.random.Random

class EightAppleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var count: Int = 0
    private var drawable: Drawable? = null
    private var drawableHeight = ScreenUtils.dip2px(80f)
    private var drawableWidth = 0
    private var widgetCount: Int = 0
    private val viewList = ArrayList<WscnImageView>()
    private var isStart : Boolean = false

    init {
        drawable = ResourceUtils.getResDrawableFromID(R.drawable.eight_apple)
        drawableWidth = (drawable!!.intrinsicWidth * drawableHeight) / drawable!!.intrinsicHeight
    }

    fun start() {
        count = 0
        if (childCount < widgetCount * 2) {
            (childCount until (widgetCount * 2))?.forEach {
                val params = LayoutParams(drawableWidth, drawableHeight)
                val imageView = WscnImageView(context)

                ImageLoadManager.loadCircleImage(R.drawable.eight_apple, imageView, 0, 0)
                addView(imageView, params)
                imageView?.setOnClickListener {
                    try {
                        it.animation?.cancel()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    it.visibility = View.INVISIBLE
                    count += 1
                }
            }
        }

        viewList.clear()
        (0 until childCount)?.forEach {
            viewList.add(getChildAt(it) as WscnImageView)
        }

        requestData {
            while (viewList.size > 0) {
                val firstView = viewList[0]
                viewList.remove(firstView)
                val randomX = Random.nextInt(measuredWidth - drawableWidth)
                firstView.layout(randomX, -drawableHeight, randomX + drawableWidth, 0)
                addAnimation(firstView)
                delay(800L)
            }
        }
    }

    private fun addAnimation(imageView: WscnImageView) {
        val scaleXAnimation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        scaleXAnimation.repeatCount = -1
        val scaleYAnimation = ObjectAnimator.ofFloat(
            imageView,
            "translationY",
            -drawableHeight * 1f,
            measuredHeight * 1f + drawableHeight
        )
        val set1 = AnimatorSet()
        set1.playTogether(scaleXAnimation, scaleYAnimation)
        set1.duration = widgetCount * 1000L
        set1.interpolator = LinearInterpolator()
        scaleYAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                imageView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                viewList.add(imageView)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        set1.start()
    }

    fun end() {

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widgetCount = Math.floor(measuredHeight * 1.0 / (drawableHeight * 2)).toInt()
        if(widgetCount > 0 && !isStart){
            isStart = true
            start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        (0 until childCount)?.forEach {
            getChildAt(it)?.layout(0, -drawableHeight, drawableWidth, 0)
        }
    }
}