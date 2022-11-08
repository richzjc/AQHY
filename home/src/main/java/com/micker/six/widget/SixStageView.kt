package com.micker.six.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.micker.aqhy.util.playErrorSuccAlarm
import com.micker.helper.system.ScreenUtils
import com.micker.six.model.SixListModel
import com.micker.six.model.SixModel
import com.pawegio.kandroid.sp
import kotlin.math.abs
import kotlin.random.Random

class SixStageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var listModel: SixListModel? = null
    private var startIndex = 0
    private var paint: Paint? = null
    private var linePaint: Paint? = null
    private var paths: ArrayList<Path>? = null
    private var lineGap: Int = 0
    private var tempStr = "大"
    private var showCount = 0
    private var leftPadding = 0
    private var rightPadding = 0
    private var bottomPadding = 0
    private var resultRectList: ArrayList<Rect>? = null
    private var rectMap: HashMap<Rect, Rect>? = null
    private var wordHeight = 0
    private var sixModelList: ArrayList<SixModel>? = null
    private var outerLeftBaseLineList = ArrayList<Int>()
    private var outerRightBaseLineList = ArrayList<Int>()
    private var resultRect: Rect? = null
    private var resultLeftRect: Rect? = null

    private var path: Path? = null
    private var downX: Float = 0f
    private var downY: Float = 0f

    init {
        paint = Paint()
        linePaint = Paint()
        paint?.isAntiAlias = true
        paint?.textSize = sp(26).toFloat()
        linePaint?.isAntiAlias = true
        paint?.isFakeBoldText = true
        paint?.color = Color.parseColor("#333333")
        linePaint?.color = Color.BLACK
        linePaint?.style = Paint.Style.STROKE
        linePaint?.strokeWidth = ScreenUtils.dip2px(3f).toFloat()
        lineGap = ScreenUtils.dip2px(40f)
        leftPadding = ScreenUtils.dip2px(10f)
        rightPadding = ScreenUtils.dip2px(20f)
        bottomPadding = ScreenUtils.dip2px(80f)
        rectMap = HashMap()
        paths = ArrayList()
        resultRectList = ArrayList()
    }

    fun bindData(listModel: SixListModel) {
        this.listModel = listModel
        startIndex = 0
        paths?.clear()
        rectMap?.clear()
        resultRectList?.clear()
        resultRect = null
        resultLeftRect = null
        sixModelList?.clear()
        outerLeftBaseLineList?.clear()
        outerRightBaseLineList?.clear()
        initSixModelList(true)
        invalidate()
    }

    fun reset() {
        paths?.clear()
        rectMap?.clear()
        sixModelList?.clear()
        resultRectList?.clear()
        resultRect = null
        resultLeftRect = null
        outerLeftBaseLineList?.clear()
        outerRightBaseLineList?.clear()
        initSixModelList(true)
        invalidate()
    }

    fun next() {
        if(listModel?.list != null){
            if(startIndex + showCount < listModel!!.list.size)
                startIndex += showCount
            else
                startIndex = showCount - (listModel!!.list.size - startIndex)
        }else{
            startIndex = 0
        }
        paths?.clear()
        rectMap?.clear()
        sixModelList?.clear()
        resultRectList?.clear()
        resultRect = null
        resultLeftRect = null
        outerLeftBaseLineList?.clear()
        outerRightBaseLineList?.clear()
        initSixModelList(true)
        invalidate()
    }


    private fun initSixModelList(isForce: Boolean) {
        if (showCount > 0 && (sixModelList == null || sixModelList!!.isEmpty() || isForce) && listModel?.list != null) {
            var sixModelList = ArrayList<SixModel>()
            if (showCount >= listModel!!.list.size)
                sixModelList.addAll(listModel!!.list)
            else {
                val endIndex = startIndex + showCount
                if (endIndex <= listModel!!.list.size) {
                    sixModelList.addAll(
                        listModel!!.list.subList(
                            startIndex,
                            startIndex + showCount
                        )
                    )
                } else {
                    sixModelList.addAll(listModel!!.list.subList(startIndex, listModel!!.list.size))
                    sixModelList.addAll(
                        listModel!!.list.subList(
                            0,
                            showCount - (listModel!!.list.size - startIndex)
                        )
                    )
                }
            }
            this.sixModelList = sixModelList
            initBaseLine()
        }
    }

    private fun initBaseLine() {
        if (sixModelList != null && sixModelList!!.isNotEmpty()) {
            var leftBaseLineList = ArrayList<Int>()
            var rightBaseLineList = ArrayList<Int>()
            var startBaseLine = rightPadding + wordHeight
            sixModelList?.forEach {
                leftBaseLineList.add(startBaseLine)
                rightBaseLineList.add(startBaseLine)
                startBaseLine += (lineGap + wordHeight)
            }


            val temp = ScreenUtils.dip2px(15f)

            sixModelList?.forEach {
                val leftRandIndex = Random.nextInt(leftBaseLineList.size)
                val rightRandIndex = Random.nextInt(rightBaseLineList.size)
                outerLeftBaseLineList?.add(leftBaseLineList[leftRandIndex])


                outerRightBaseLineList?.add(rightBaseLineList[rightRandIndex])

                val leftRect = Rect()
                paint?.getTextBounds(it.leftTitle, 0, it.leftTitle.length, leftRect)

                val rightRect = Rect()
                paint?.getTextBounds(it.rightTitle, 0, it.rightTitle.length, rightRect)

                val realLeftRect = Rect()
                realLeftRect.left = 0
                realLeftRect.top = leftBaseLineList[leftRandIndex] - wordHeight - temp
                realLeftRect.right = leftPadding + abs(leftRect.right - leftRect.left) + rightPadding
                realLeftRect.bottom = leftBaseLineList[leftRandIndex] + temp

                val realRightRect = Rect()
                realRightRect.left =
                    measuredWidth - rightPadding - abs(rightRect.right - rightRect.left) - leftPadding
                realRightRect.top = rightBaseLineList[rightRandIndex] - wordHeight - temp
                realRightRect.right = measuredWidth
                realRightRect.bottom = rightBaseLineList[rightRandIndex] + temp


                rectMap?.put(realLeftRect, realRightRect)

                leftBaseLineList.removeAt(leftRandIndex)
                rightBaseLineList.removeAt(rightRandIndex)
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, realHeightMeasureSpec)
        if (wordHeight <= 0) {
            var rect = Rect()
            paint?.getTextBounds(tempStr, 0, tempStr.length, rect)
            wordHeight = abs(rect.bottom - rect.top)
            showCount =
                Math.floor((measuredHeight * 1.0 - leftPadding  - bottomPadding) / (lineGap + abs(rect.bottom - rect.top)))
                    .toInt()
            if(showCount > 7)
                showCount = 7
        }

        initSixModelList(false)
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            path = null
            downX = event.x
            downY = event.y
            if (!checkHasDraw(downX, downY) && checkClickInRect(downX, downY)) {
                path = Path()
                path?.moveTo(downX, downY)
            }
        } else if (event?.action == MotionEvent.ACTION_UP) {
            path?.lineTo(event!!.x, event.y)
            if (checkResultIsSucc(
                    event.x,
                    event.y
                ) && resultLeftRect != null && resultRect != null
            ) {
                path = Path()
                path?.moveTo(
                    resultLeftRect!!.right.toFloat(),
                    (resultLeftRect!!.top + resultLeftRect!!.bottom).toFloat() / 2
                )
                path?.lineTo(
                    resultRect!!.left.toFloat(),
                    (resultRect!!.top + resultRect!!.bottom).toFloat() / 2
                )
                paths?.add(path!!)
                resultRectList?.add(resultLeftRect!!)
                path = null
                playErrorSuccAlarm(getContext(), true)
            } else {
                path = null
                playErrorSuccAlarm(getContext(), false)
            }

        } else if (event?.action == MotionEvent.ACTION_MOVE) {
            path?.lineTo(event!!.x, event.y)
        } else {
            path = null
        }
        invalidate()
        return true
    }

    private fun checkClickInRect(downx: Float, downY: Float): Boolean {
        var result = false
        rectMap?.keys?.forEach {
            if (downx >= it.left && downx <= it.right && downY >= it.top && downY <= it.bottom) {
                result = true
                resultRect = rectMap?.get(it)
                resultLeftRect = it
            }
        }
        return result
    }

    private fun checkResultIsSucc(downx: Float, downY: Float): Boolean {
        var result = false
        if (resultRect != null && downx >= resultRect!!.left && downx <= resultRect!!.right && downY >= resultRect!!.top && downY <= resultRect!!.bottom) {
            result = true
        }
        return result
    }

    private fun checkHasDraw(downx: Float, downY: Float): Boolean {
        var result = false
        resultRectList?.forEach {
            if (downx >= it.left && downx <= it.right && downY >= it.top && downY <= it.bottom) {
                result = true
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (listModel != null && listModel!!.list != null) {
            sixModelList?.forEachIndexed { index, it ->
                canvas?.drawText(
                    it.leftTitle,
                    leftPadding.toFloat(),
                    outerLeftBaseLineList.get(index).toFloat(),
                    paint!!
                )

                val rightRect = Rect()
                paint?.getTextBounds(it.rightTitle, 0, it.rightTitle.length, rightRect)
                canvas?.drawText(
                    it.rightTitle,
                    (measuredWidth - leftPadding - abs(rightRect.right - rightRect.left)).toFloat(),
                    outerRightBaseLineList.get(index).toFloat(),
                    paint!!
                )

            }
        }

        paths?.forEach {
            canvas?.drawPath(it, linePaint!!)
        }

        if (path != null)
            canvas?.drawPath(path!!, linePaint!!)
    }
}