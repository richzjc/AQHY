package com.micker.four.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.micker.aqhy.util.playClickAlarm
import com.micker.aqhy.util.playErrorSuccAlarm
import com.micker.core.imageloader.WscnImageView
import com.micker.first.callback.SuccCallback
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlin.math.min
import kotlin.random.Random

class FourStageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var imageView: ImageView? = null

    var jieShu: Int = 3
    var lineWidth = ScreenUtils.dip2px(1.5f)
    var horlineViewList = ArrayList<View>()
    var verlineViewList = ArrayList<View>()
    var wordViewList = ArrayList<ArrayList<WscnImageView>>()
    var originBitmap: Bitmap? = null

    val cutMap = HashMap<WscnImageView?, Bitmap?>()
    val orderBitmapList = ArrayList<Bitmap>()
    var succCallback: SuccCallback? = null

    var emptShareTv: WscnImageView? = null
    var lastBitmap: Bitmap? = null

    private val onClickListener by lazy {
        object : OnClickListener {
            @Synchronized
            override fun onClick(it: View?) {
                if (it is WscnImageView && emptShareTv != null && it != emptShareTv) {
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
                            var rankFlag =
                                (clickRank >= emptyRank - 1) && (clickRank <= emptyRank + 1)
                            var totalFlag = true
                            if (clickRow != emptyRow)
                                totalFlag = (clickRank == emptyRank)
                            if (rowFlag && rankFlag && totalFlag) {
                                try {
                                    val bitmap = cutMap[it]
                                    if (bitmap != null && !bitmap.isRecycled) {
                                        emptShareTv?.setImageBitmap(bitmap)
                                        it?.setImageBitmap(null)
                                    }
                                    cutMap[emptShareTv] = bitmap
                                    cutMap[it] = null
                                    emptShareTv = it
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                if (emptShareTv == lastShareTv) {
                                    if (checkResult()) {
                                        if (lastBitmap != null && !lastBitmap!!.isRecycled)
                                            emptShareTv?.setImageBitmap(lastBitmap)
                                        playErrorSuccAlarm(getContext(), true)
                                        //TODO 添加烟花效果
                                    } else {
                                        playClickAlarm(getContext())
                                    }
                                } else {
                                    playClickAlarm(getContext())
                                }


                            } else {
                                playClickAlarm(getContext())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun queryTag(view: WscnImageView?): IntArray? {
        var intArr: IntArray? = null
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


    private fun checkResult(): Boolean {
        var flag = true
        var startIndex = 0
        wordViewList?.forEachIndexed { outer, arrayList ->
            arrayList.forEachIndexed { inner, wscnImageView ->
                if (!(outer == jieShu - 1 && inner == jieShu - 1)) {
                    val curBitmap = cutMap[wscnImageView]
                    if (!flag || startIndex >= orderBitmapList.size || curBitmap != orderBitmapList[startIndex])
                        flag = false
                }


                startIndex += 1
            }

        }
        return flag
    }

    private fun cutBitmap(bitmap: Bitmap) {
        val width = bitmap.width
        val height = bitmap.height
        val itemBitmapWdith = width / jieShu
        val itemBitmapHeight = height / jieShu
        val total = (jieShu * jieShu)
        (0 until total)?.forEach {
            var row = it / jieShu
            var rank = it % jieShu
            var startX = rank * itemBitmapWdith
            var startY = row * itemBitmapHeight
            val cutBitmap =
                Bitmap.createBitmap(bitmap, startX, startY, itemBitmapWdith, itemBitmapHeight)
            if (it == total - 1) {
                lastBitmap = cutBitmap
            } else {
                orderBitmapList.add(cutBitmap)
            }
        }
    }

    fun bindData(jieShu1: Int, bitmap: Bitmap?, succCallback: SuccCallback) {
        bitmap ?: return
        originBitmap = bitmap
        this.succCallback = succCallback
        this.jieShu = jieShu1
        removeAllViews()
        horlineViewList?.clear()
        verlineViewList?.clear()
        wordViewList?.clear()
        cutMap.clear()
        orderBitmapList.clear()
        cutBitmap(bitmap!!)

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


            var realBmpList = ArrayList(orderBitmapList)
            wordViewList?.forEachIndexed { outerIndex, arrayList ->
                arrayList.forEachIndexed { innerIndex, it ->
                    if (!(outerIndex == jieShu - 1 && innerIndex == jieShu - 1)) {
                        val numberListSize = realBmpList.size
                        val realIndex = Random.nextInt(numberListSize)
                        val bitmap = realBmpList[realIndex]
                        it.setImageBitmap(bitmap)
                        realBmpList.removeAt(realIndex)
                        it?.also {
                            addView(it, params)
                        }

                        cutMap[it] = bitmap
                    }

                    it?.setOnClickListener(onClickListener)
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

            if (emptShareTv != null && emptShareTv?.parent == null) {
                addView(emptShareTv)
                emptShareTv?.setOnClickListener(onClickListener)
            }
        }
    }

    private fun createShareTextView(): WscnImageView {
        val shareTextView = WscnImageView(context)
        shareTextView.scaleType = ImageView.ScaleType.FIT_XY
        shareTextView.hierarchy?.setPlaceholderImage(ColorDrawable(Color.TRANSPARENT))
        return shareTextView
    }

    private fun createLineView(): View {
        val view = View(context)
        view.setBackgroundColor(Color.parseColor("#eeeeee"))
        return view
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (jieShu > 0 && originBitmap != null) {
            var widthSize1 = MeasureSpec.getSize(widthMeasureSpec)
            val heightSize = MeasureSpec.getSize(heightMeasureSpec)
            val tempHeight = (originBitmap!!.height * 1f / originBitmap!!.width) * widthSize1

            var realHeightSize: Int
            var widthSize: Int


            if (tempHeight > heightSize) {
                realHeightSize = heightSize
                widthSize =
                    ((originBitmap!!.width * 1f / originBitmap!!.height) * realHeightSize).toInt()
            } else {
                widthSize = widthSize1
                realHeightSize =
                    ((originBitmap!!.height * 1f / originBitmap!!.width) * widthSize1).toInt()
            }


            val itemViewWidth = (widthSize - (jieShu - 1) * lineWidth) / jieShu
            val itemViewHeight = (realHeightSize - (jieShu - 1) * lineWidth) / jieShu
            val withspec = MeasureSpec.makeMeasureSpec(itemViewWidth, MeasureSpec.EXACTLY)
            val heightspec = MeasureSpec.makeMeasureSpec(itemViewHeight, MeasureSpec.EXACTLY)
            wordViewList?.forEach {
                it.forEach {
                    it.measure(withspec, heightspec)
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