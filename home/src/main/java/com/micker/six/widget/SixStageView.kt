package com.micker.six.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
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
    private var padding = 0
    private var leftRectList: List<Rect>? = null
    private var rightRectList: List<Rect>? = null
    private var resultRectList: List<Rect>? = null
    private var rectMap: HashMap<Rect, Rect>? = null
    private var wordHeight = 0

    init {
        paint = Paint()
        linePaint = Paint()
        paint?.isAntiAlias = true
        paint?.textSize = sp(26).toFloat()
        linePaint?.isAntiAlias = true
        paint?.isFakeBoldText = true
        paint?.color = Color.parseColor("#333333")
        linePaint?.color = Color.BLACK
        linePaint?.strokeWidth = ScreenUtils.dip2px(5f).toFloat()
        lineGap = ScreenUtils.dip2px(40f)
        padding = ScreenUtils.dip2px(10f)
        rectMap = HashMap()
        paths = ArrayList()
    }

    fun bindData(listModel: SixListModel) {
        this.listModel = listModel
        startIndex = 0
        paths?.clear()
        rectMap?.clear()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, realHeightMeasureSpec)
        var rect = Rect()
        paint?.getTextBounds(tempStr, 0, tempStr.length, rect)
        wordHeight = abs(rect.bottom - rect.top)
        showCount =
            Math.floor((measuredHeight * 1.0 - padding * 2) / (lineGap + abs(rect.bottom - rect.top)))
                .toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //TODO 选中的点没有被连接过，且在左边的矩形区域里面

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (listModel != null && listModel!!.list != null) {
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

            var leftBaseLineList = ArrayList<Int>()
            var rightBaseLineList = ArrayList<Int>()
            var startBaseLine = padding + wordHeight
            sixModelList?.forEach {
                leftBaseLineList.add(startBaseLine)
                rightBaseLineList.add(startBaseLine)
                startBaseLine += (lineGap + wordHeight)
            }

            sixModelList?.forEach {
                val leftRandIndex = Random.nextInt(leftBaseLineList.size)
                val rightRandIndex = Random.nextInt(rightBaseLineList.size)
                canvas?.drawText(it.leftTitle, padding.toFloat(), leftBaseLineList[leftRandIndex].toFloat(), paint!!)
                leftBaseLineList.removeAt(leftRandIndex)


                val rightRect = Rect()
                paint?.getTextBounds(it.rightTitle, 0, it.rightTitle.length, rightRect)
                canvas?.drawText(it.rightTitle, (measuredWidth - padding - abs(rightRect.right - rightRect.left)).toFloat(), rightBaseLineList[rightRandIndex].toFloat(), paint!!)
                rightBaseLineList.removeAt(rightRandIndex)
            }
        }
    }
}