package com.micker.third.widget

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.micker.aqhy.util.playClickAlarm
import com.micker.aqhy.util.playErrorSuccAlarm
import com.micker.core.widget.ShareTextView
import com.micker.first.callback.SuccCallback
import com.micker.helper.TLog
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlin.math.min
import kotlin.random.Random

class ThirdStageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var imageView: ImageView? = null

    private var isEditMode = true
    var jieShu: Int = 3
    var lineWidth = ScreenUtils.dip2px(1f)
    var horlineViewList = ArrayList<View>()
    var verlineViewList = ArrayList<View>()
    var wordViewList = ArrayList<ArrayList<ShareTextView>>()

    var resultStr = ""
    var succCallback: SuccCallback? = null

    var emptShareTv : ShareTextView? = null

    private val onClickListener by lazy {
        object : OnClickListener{
            @Synchronized
            override fun onClick(it: View?) {
                if (it is ShareTextView && isEditMode && emptShareTv != null && it != emptShareTv) {
                    val emptyTag = queryTag(emptShareTv)
                    if (emptyTag != null) {
                        var emptyRow = emptyTag[0]
                        var emptyRank = emptyTag[1]

                        val lastShareTv = wordViewList[jieShu - 1][jieShu - 1]
                        val clickTag = queryTag(it)
                        if (clickTag != null) {
                            var clickRow = clickTag[0]
                            var clickRank = clickTag[1]

                            var rowFlag = (clickRow >= emptyRow - 1) && (clickRow <= emptyRow + 1)
                            var rankFlag = (clickRank >= emptyRank - 1) && (clickRank <= emptyRank + 1)
                            var totalFlag = true
                            if(clickRow != emptyRow)
                                totalFlag = (clickRank == emptyRank)
                            if (rowFlag && rankFlag && totalFlag) {
                                try {
                                    val replaceStr = it?.text
                                    emptShareTv?.setText(replaceStr)
                                    it?.text = ""
                                    emptShareTv = it
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                if (emptShareTv == lastShareTv) {
                                    if (TextUtils.equals(checkResult(), resultStr)) {
                                        playErrorSuccAlarm(getContext(), true)
                                    } else {
                                        playClickAlarm(getContext())
                                    }
                                } else {
                                    playClickAlarm(getContext())
                                }


                            }else{
                                playClickAlarm(getContext())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun queryTag(view: ShareTextView?): IntArray? {
        var intArr : IntArray? = null
        wordViewList?.forEachIndexed { outer, arrayList ->
            arrayList.forEachIndexed { index, shareTextView ->
                if (shareTextView == view) {
                    intArr = IntArray(2)
                    intArr!![0] = outer
                    intArr!![1] = index
                }
            }
        }
        return intArr
    }


    private fun checkResult(): String {
        val builder = SpannableStringBuilder()
        wordViewList?.forEach {
            it.forEach {
                builder.append(it.text)
            }
        }
        return builder.toString()
    }


    fun bindData(jieShu1: Int, isEditMode: Boolean, succCallback: SuccCallback) {
        this.succCallback = succCallback
        this.jieShu = jieShu1
        this.isEditMode = isEditMode
        removeAllViews()
        horlineViewList?.clear()
        verlineViewList?.clear()
        wordViewList?.clear()
        resultStr = ""

        imageView = ImageView(getContext())
        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        imageView?.scaleType = ImageView.ScaleType.FIT_XY
        imageView?.setImageResource(R.drawable.third_stage_bg)
        addView(imageView, params)

        if (jieShu < 3)
            jieShu = 3
        else if (jieShu > 6)
            jieShu = 6

        if (jieShu > 0) {
            val total = jieShu
            (0 until total)?.forEach { outer ->
                wordViewList.add(ArrayList())
                var outerList = wordViewList.get(outer)
                (0 until total)?.forEach { inner ->
                    val createView = createShareTextView()
                    outerList.add(createView)
                }
            }

            emptShareTv = wordViewList[jieShu - 1][jieShu - 1]

            val realTotal = jieShu * jieShu
            val spanBuilder = SpannableStringBuilder()
            val numberList = ArrayList<Int>()
            (1 until realTotal)?.forEach {
                spanBuilder.append("${it}")
                numberList.add(it)
            }
            resultStr = spanBuilder.toString()?.trim()
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

            if (isEditMode) {
                wordViewList?.forEachIndexed { outerIndex, it ->
                    it.forEachIndexed { inndexIndex, it ->
                        if (it != emptShareTv) {
                            val numberListSize = numberList.size
                            val realIndex = Random.nextInt(numberListSize)
                            it?.text = "${numberList.get(realIndex)}"
                            numberList.removeAt(realIndex)
                            if (isEditMode) {
                                it.setOnClickListener(onClickListener)
                            }
                            it?.also {
                                addView(it, params)
                            }
                        }
                    }
                }
            } else {
                var startIndex = 1
                wordViewList?.forEach {
                    it.forEach {
                        if (it != emptShareTv) {
                            it?.text = "${startIndex}"
                            startIndex += 1
                            it?.also {
                                addView(it)
                            }
                        }

                        it?.setOnClickListener(onClickListener)
                    }
                }
            }

            val lineTotal = (jieShu - 1)
            (0 until lineTotal)?.forEach {
                val lineView = createLineView()
                horlineViewList.add(lineView)

                val verlineView = createLineView()
                verlineViewList.add(verlineView)

                addView(lineView)
                addView(verlineView)
            }

            if(emptShareTv != null && emptShareTv?.parent == null){
                addView(emptShareTv)
                emptShareTv?.setOnClickListener(onClickListener)
            }
        }
    }

    private fun createShareTextView(): ShareTextView {
        val shareTextView = ShareTextView(context)
        shareTextView.gravity = Gravity.CENTER
        shareTextView.setTextColor(Color.WHITE)
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
            if (!isEditMode) {
                widthSize = (ScreenUtils.getScreenWidth() / 3)
            }

            var realHeightSize = widthSize

            val itemViewWidth = (widthSize - (jieShu - 1) * lineWidth) / jieShu
            val itemViewHeight = (realHeightSize - (jieShu - 1) * lineWidth) / jieShu
            val withspec = MeasureSpec.makeMeasureSpec(itemViewWidth, MeasureSpec.EXACTLY)
            val heightspec = MeasureSpec.makeMeasureSpec(itemViewHeight, MeasureSpec.EXACTLY)
            wordViewList?.forEach {
                it.forEach {
                    it.measure(withspec, heightspec)
                    it.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        min(itemViewHeight, itemViewWidth) * 2.0f / 3.0f
                    )
                }
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


            val ivwidthspec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
            val ivHeightspec = MeasureSpec.makeMeasureSpec(realHeightSize, MeasureSpec.EXACTLY)
            imageView?.measure(ivwidthspec, ivHeightspec)

            setMeasuredDimension(widthSize, realHeightSize)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (wordViewList.size > 0 && wordViewList.get(0).size > 0) {

            imageView?.layout(0, 0, measuredWidth, measuredHeight)

            wordViewList?.forEachIndexed { outerIndex, shareTextView ->
                shareTextView?.forEachIndexed { index, it ->
                    val mwidth = it.measuredWidth
                    val mHeight = it.measuredHeight
                    val startX = (index % jieShu) * (mwidth + lineWidth)
                    val startY = outerIndex * (mHeight + lineWidth)
                    it.layout(
                        startX,
                        startY.toInt(),
                        startX + mwidth,
                        (startY + mHeight).toInt()
                    )
                }
            }


            if (jieShu > 0) {
                val wordWidth = wordViewList[0][0].measuredWidth
                val wordHeight = wordViewList[0][0].measuredHeight
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