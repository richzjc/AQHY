package com.micker.home.utils

import android.graphics.Bitmap
import android.util.Log
import com.micker.core.base.BaseActivity
import com.micker.helper.file.QDUtil
import com.micker.webview.Widget.WSCNWebView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun isSinglePixel(view: WSCNWebView?): Disposable? {
    val disposable = Observable.interval(500, 500, TimeUnit.MILLISECONDS)
        .takeUntil {
            val bitmap = QDUtil.convertViewToBitmap(view)
            val flag = if (bitmap == null)
                true
            else {
                checkIsSigle(bitmap!!)
            }
            bitmap?.recycle()
            flag
        }
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally {
            (view?.context as? BaseActivity<*, *>)?.dismissDialog()

        }
        .subscribe()
    return disposable
}

private fun checkIsSigle(bitmap: Bitmap): Boolean {
    val widht = bitmap.width
    val height = bitmap.height
    var flag = false
    if (height > 0 && widht > 0) {
        val halfHeight = height / 2
        var halfWidht = widht / 2
        val times = Math.min(halfWidht, 300)
        val firstColr = bitmap.getPixel(halfWidht, halfHeight)
        var nextColor: Int
        (1..times).forEachIndexed { index, i ->
            nextColor = bitmap.getPixel(halfWidht - index, halfHeight)
            if (firstColr != nextColor) {
                flag = true
                Log.i("color", "nextColor = $nextColor")
                return@forEachIndexed
            }

            nextColor = bitmap.getPixel(halfWidht + index, halfHeight)
            if (firstColr != nextColor) {
                flag = true
                return@forEachIndexed
            }
        }
    }
    return flag
}