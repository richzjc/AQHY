package com.micker.seven.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.micker.first.widget.TianTextView
import com.micker.helper.ResourceUtils
import com.micker.helper.speak.TextToSpeechUtils.textSpeak
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.wallstreetcn.rpc.coroutines.requestData
import kotlinx.coroutines.delay

class SevenPoetryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var list: List<String>? = null
    private var outerIndex: Int = -1
    private var innerIndex: Int = -1
    private var isPlay: Boolean = false

    init {
        orientation = VERTICAL
    }

    fun bindData(list: List<String>) {
        this.list = list
        outerIndex = -1
        innerIndex = -1
        removeAllViews()
        if (list != null) {
            list.forEachIndexed { index, value ->
                val view = SevenPoetryRowView(context)
                val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if (index > 0)
                    params.topMargin = ScreenUtils.dip2px(15f)
                view.bindData(value)
                addView(view, params)
            }
        }
    }

    fun bindDataTest(list: List<String>) {
        this.list = list
        outerIndex = -1
        innerIndex = -1
        removeAllViews()
        if (list != null) {
            list.forEachIndexed { index, value ->
                val view = SevenPoetryRowView(context)
                val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if (index > 0)
                    params.topMargin = ScreenUtils.dip2px(15f)
                view.bindDataTest(value)
                addView(view, params)
            }
        }
    }

    fun updatePlayStatus(isPlay: Boolean) {
        this.isPlay = isPlay
        if (isPlay) {
            play()
        }
    }


    private fun play() {
        requestData {
            var realOuterIndex = outerIndex
            var realInnerIndex = innerIndex
            if (outerIndex <= -1) {
                realOuterIndex = 0
                realInnerIndex = 0
            }
            val splitStr = "。，？；！、"
            (realOuterIndex until childCount)?.forEach {
                if(!isPlay)
                    return@forEach
                outerIndex = it
                val rowView = getChildAt(it) as SevenPoetryRowView
                val rowChildCount = rowView.childCount
                (realInnerIndex until rowChildCount)?.forEach {
                    if(!isPlay)
                        return@forEach

                    val tianTv = rowView.getChildAt(it) as? TianTextView
                    val text = tianTv?.text
                    if(isPlay && !TextUtils.isEmpty(text) && !splitStr.contains(text!!)){
                        innerIndex = it
                        textSpeak(text.toString())
                        playAnimation(tianTv)
                        delay(1000L)

                        if(outerIndex == childCount - 1 && innerIndex >= (rowView.childCount - 2)){
                            outerIndex = -1
                            innerIndex = -1
                        }

                    }
                }
                realInnerIndex = 0
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isPlay = false
    }

    private fun playAnimation(tianTv: TianTextView) {
        val scaleXAnimation = ObjectAnimator.ofFloat(tianTv, "scaleX", 1f, 1.4f)
        val scaleYAnimation = ObjectAnimator.ofFloat(tianTv, "scaleY", 1f, 1.4f)
        val set1 = AnimatorSet()
        set1.playTogether(scaleXAnimation, scaleYAnimation)
        set1.setDuration(400L)
        set1.interpolator = LinearInterpolator()


        val scaleXAnimation1 = ObjectAnimator.ofFloat(tianTv, "scaleX", 1.4f, 1f)
        val scaleYAnimation1 = ObjectAnimator.ofFloat(tianTv, "scaleY", 1.4f, 1f)
        val set2 = AnimatorSet()
        set2.playTogether(scaleXAnimation1, scaleYAnimation1)
        set2.setDuration(400L)
        set2.interpolator = LinearInterpolator()

        val totalSet = AnimatorSet()
        totalSet.playSequentially(set1, set2)
        totalSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
                tianTv.setTextColor(ResourceUtils.getColor(R.color.day_mode_theme_color_1478f0))
            }

            override fun onAnimationEnd(animation: Animator?) {
                tianTv.setTextColor(ResourceUtils.getColor(R.color.day_mode_text_color1_333333))
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        totalSet.start()
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