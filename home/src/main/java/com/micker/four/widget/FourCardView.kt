package com.micker.four.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class FourCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    var originBitmap: Bitmap? = null
    fun bindBitmap(bindBitmap: Bitmap) {
        this.originBitmap = bindBitmap
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (originBitmap != null) {
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
            setMeasuredDimension(widthSize, realHeightSize)
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}