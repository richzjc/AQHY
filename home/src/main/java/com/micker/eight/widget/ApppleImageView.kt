package com.micker.eight.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.micker.core.imageloader.WscnImageView
import com.micker.home.R

class ApppleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : WscnImageView(context, attrs) {
    private var mPaint: Paint? = null
    private var mBitmap: Bitmap? = null
    private var d = 16
    private var mBalls: ArrayList<Ball>? = null
    private var isCanClick = true
    private var animator: ValueAnimator? = null

    init {
        mPaint = Paint()
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.eight_apple)
        mBalls = ArrayList()
    }

    private fun initBalls() {
        mBalls?.clear()
        (0 until mBitmap!!.width)?.forEach { i ->
            val height = mBitmap!!.height - 1
            for (j in 0..height step d) {
                val ball = Ball()
                ball.color = mBitmap!!.getPixel(i, j)
                ball.r = d / 2f
                ball.x = i + ball.r
                ball.y = j + ball.r
                ball.vx = ((Math.random() - 0.5f) * 40f).toFloat()
                ball.vy = ((Math.random() - 0.5f) * 60f).toFloat()
                ball.ax = 0f
                ball.ay = 9.8f
                mBalls?.add(ball)
            }
        }
    }

    private fun updateBall() {
        mBalls?.forEach {
            it.x += it.vx
            it.y += it.vy
            it.vx += it.ax
            it.vy += it.ay
        }
    }

    private fun startAnimator() {
        try {
            animator?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            animation?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        animator = ValueAnimator.ofFloat(0f, 1f)
        animator?.duration = 1000
        animator?.interpolator = LinearInterpolator()
        animator?.addUpdateListener {
            updateBall()
            invalidate()
        }
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                isCanClick = true
                visibility = View.INVISIBLE
                val parentView = parent as? EightAppleView
                if (parentView != null)
                    parentView.count += 1
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        animator?.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (isCanClick) {
                initBalls()
                invalidate()
                startAnimator()
            }
            isCanClick = false
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        if (isCanClick)
            super.onDraw(canvas)
        else {
            mBalls?.forEach {
                mPaint?.color = it.color
                canvas?.drawCircle(it.x, it.y, it.r, mPaint!!)
            }
        }
    }
}