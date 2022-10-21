package com.micker.first.widget

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.micker.aqhy.util.playErrorSuccAlarm
import com.micker.core.widget.ShareTextView
import com.micker.first.callback.SuccCallback
import com.micker.helper.ResourceUtils
import com.micker.helper.TLog
import com.micker.helper.snack.MToastHelper
import com.micker.helper.speak.TextToSpeechUtils.textSpeak
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlin.math.min
import kotlin.random.Random

class FirstStageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        setBackgroundResource(R.drawable.shape_first_stage_bg)
    }

    var jieShu: Int = 3
    var findWord: String = ""
    var proguardWord: String = ""
    var lineWidth = ScreenUtils.dip2px(1f)
    var horlineViewList = ArrayList<View>()
    var verlineViewList = ArrayList<View>()
    var wordViewList = ArrayList<ShareTextView>()
    var succCallback: SuccCallback? = null

    private val onClickListener by lazy {
        OnClickListener {
            if (it is ShareTextView) {
                val tvValue = it.text?.toString()?.trim()
                if (TextUtils.equals(tvValue, findWord)) {
                    playErrorSuccAlarm(getContext(), true)
                    postDelayed({
                        succCallback?.succCallback()
                    }, 1500)

                } else {
                    playErrorSuccAlarm(getContext(), false)
                }
            }
        }
    }


    fun bindData(jieShu1: Int, findWord: String, proguardWord: String, succCallback: SuccCallback) {
        textSpeak("找出${findWord}")
        this.jieShu = jieShu1
        this.findWord = findWord
        this.proguardWord = proguardWord
        this.succCallback = succCallback
        removeAllViews()
        if (jieShu < 3)
            jieShu = 3
        else if (jieShu > 11)
            jieShu = 11


        if (jieShu > 0) {
            val total = jieShu * jieShu
            (0 until total)?.forEach {
                if (wordViewList.size < total) {
                    val createView = createShareTextView()
                    wordViewList.add(createView)
                }
            }

            if (wordViewList.size > total) {
                val subList = wordViewList.subList(0, total)
                wordViewList = ArrayList(subList)
            }

            wordViewList.forEach {
                it.text = proguardWord
                addView(it)
                it.setOnClickListener(onClickListener)
            }

            val nextInt = Random.nextInt(wordViewList.size)
            wordViewList[nextInt].text = findWord


            val lineTotal = (jieShu - 1)
            (0 until lineTotal)?.forEach {
                if (horlineViewList.size < lineTotal) {
                    val lineView = createLineView()
                    horlineViewList.add(lineView)
                }

                if (verlineViewList.size < lineTotal) {
                    val lineView = createLineView()
                    verlineViewList.add(lineView)
                }
            }

            if (horlineViewList.size > lineTotal) {
                val subList = horlineViewList.subList(0, lineTotal)
                horlineViewList = ArrayList(subList)
            }

            if (verlineViewList.size > lineTotal) {
                val subList = verlineViewList.subList(0, lineTotal)
                verlineViewList = ArrayList(subList)
            }

            horlineViewList?.forEach {
                addView(it)
            }

            verlineViewList?.forEach {
                addView(it)
            }
        }
    }

    private fun createShareTextView(): ShareTextView {
        val shareTextView = ShareTextView(context)
        shareTextView.gravity = Gravity.CENTER
        shareTextView.setTextColor(Color.parseColor("#333333"))
        shareTextView.setBackgroundResource(R.drawable.layer_drawable_first_stage_item)
        return shareTextView
    }

    private fun createLineView(): View {
        val view = View(context)
        view.setBackgroundColor(Color.parseColor("#eeeeee"))
        return view
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (jieShu > 0) {
            var widthSize = MeasureSpec.getSize(widthMeasureSpec)
            var realHeightSize = widthSize

            val itemViewWidth = (widthSize - (jieShu - 1) * lineWidth) / jieShu
            val itemViewHeight = (realHeightSize - (jieShu - 1) * lineWidth) / jieShu
            val withspec = MeasureSpec.makeMeasureSpec(itemViewWidth, MeasureSpec.EXACTLY)
            val heightspec = MeasureSpec.makeMeasureSpec(itemViewHeight, MeasureSpec.EXACTLY)
            wordViewList?.forEach {
                it.measure(withspec, heightspec)
                it.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    min(itemViewHeight, itemViewWidth) * 2.0f / 3.0f
                )
            }

            val horwidthspec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
            val horHeightspec = MeasureSpec.makeMeasureSpec(lineWidth, MeasureSpec.EXACTLY)
            horlineViewList?.forEach {
                it.measure(horwidthspec, horHeightspec)
            }

            val verwidthspec = MeasureSpec.makeMeasureSpec(lineWidth, MeasureSpec.EXACTLY)
            val verHeightspec = MeasureSpec.makeMeasureSpec(realHeightSize, MeasureSpec.EXACTLY)
            verlineViewList?.forEach {
                it.measure(verwidthspec, verHeightspec)
            }

            setMeasuredDimension(widthSize, realHeightSize)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (wordViewList.size > 0) {

            wordViewList?.forEachIndexed { index, shareTextView ->
                val startX = (index % jieShu) * (shareTextView.measuredWidth + lineWidth)
                val startY =
                    Math.floor(index * 1.0 / jieShu) * (shareTextView.measuredHeight + lineWidth)
                shareTextView.layout(
                    startX,
                    startY.toInt(),
                    startX + shareTextView.measuredWidth,
                    (startY + shareTextView.measuredHeight).toInt()
                )
            }


            if (jieShu > 0) {
                val wordWidth = wordViewList[0].measuredWidth
                val wordHeight = wordViewList[0].measuredHeight
                (0 until (jieShu - 1))?.forEach {
                    val lineStartX = 0
                    val lineStartY = (it + 1) * wordHeight + (it * lineWidth)
                    val lineView = horlineViewList[it]
                    lineView.layout(
                        lineStartX, lineStartY, lineStartX + measuredWidth,
                        (lineStartY + lineWidth)
                    )


                    var startY = 0
                    var startx = (it + 1) * wordWidth + (it * lineWidth)
                    val verlineView = verlineViewList[it]
                    verlineView.layout(
                        startx, startY, startx + lineWidth,
                        (startY + measuredHeight)
                    )
                }
            }
        }
    }
}