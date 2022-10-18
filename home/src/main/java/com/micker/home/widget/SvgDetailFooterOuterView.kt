package com.micker.home.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.core.Controller
import com.app.hubert.guide.listener.OnGuideChangedListener
import com.app.hubert.guide.model.GuidePage
import com.micker.data.model.home.ColorGroupDetailEntity
import com.micker.helper.observer.Observer
import com.micker.helper.observer.ObserverIds
import com.micker.helper.observer.ObserverManger
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.adapter.MyPageAdapter
import com.micker.home.singleton.ColorHistoryFactory
import com.micker.home.singleton.ColorsItemFactory
import com.micker.home.utils.setColors
import com.pawegio.kandroid.show
import com.richzjc.library.TabLayout
import kotlinx.android.synthetic.main.aqhy_view_footer_bottom_left_right.view.*
import kotlinx.android.synthetic.main.aqhy_view_svg_detail_footer_outer.view.*

class SvgDetailFooterOuterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Observer {

    private val adapter by lazy { MyPageAdapter() }
    private var tabSelectListener : TabLayout.ViewPagerOnTabSelectedListener? = null
    private var viewPagerListener : ViewPager.OnPageChangeListener? = null

    private val leftRotate by lazy {
        val animator = ObjectAnimator.ofFloat(leftView, "rotation", 0f, 360f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = 5000
        animator.interpolator = LinearInterpolator()
        animator
    }

    private val rightRotate by lazy {
        val animator = ObjectAnimator.ofFloat(rightView, "rotation", 0f, 360f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = 5000
        animator.interpolator = LinearInterpolator()
        animator
    }

    private var bottomViewWidth: Int = 0

    private var entity: ColorGroupDetailEntity? = null

    init {
        orientation = VERTICAL
        LayoutInflater.from(getContext()).inflate(R.layout.aqhy_view_svg_detail_footer_outer, this, true)
        ObserverManger.getInstance().registerObserver(this, ObserverIds.COLORS_ITEM_SELECTED)
        leftRotate.start()
        rightRotate.start()
        setListener()
        tabSelectListener = TabLayout.ViewPagerOnTabSelectedListener(viewPager)
        viewPagerListener = object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tabLayout?.removeOnTabSelectedListener(tabSelectListener!!)
                tabLayout?.getTabAt(position)?.select()
                tabLayout?.addOnTabSelectedListener(tabSelectListener!!)
            }
        }
        tabLayout?.addOnTabSelectedListener(tabSelectListener!!)
        viewPager?.addOnPageChangeListener(viewPagerListener!!)
    }

    private fun setListener() {
        category?.setOnClickListener {
            (parent?.parent as? SvgDetailFooterView)?.showHistoryView()
            (parent?.parent as? SvgDetailFooterView)?.historyViewShowCategoryData()
        }

        history?.setOnClickListener {
            (parent?.parent as? SvgDetailFooterView)?.showHistoryView()
            (parent?.parent as? SvgDetailFooterView)?.historyViewShowHistoryData()
        }

        rightView?.setOnClickListener {
            ColorsItemFactory.rightsColorsItem?.also {
                ColorsItemFactory.selectColorsItem(it)
            }
        }
    }

    fun bindData(entity: ColorGroupDetailEntity?) {
        this.entity = entity
        tabLayout?.setupWithTitles(entity?.titles)
        adapter.mdatas = entity?.colorList
        viewPager?.adapter = adapter
        entity?.colorList?.apply {
            if (this.isNotEmpty() && this[0].colors != null && this[0].colors!!.isNotEmpty())
                ColorsItemFactory.selectColorsItem(this[0].colors!![0])
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i("ballWidth", "${viewPager.height}")
        if (viewPager.height > 0 && bottomViewWidth == 0) {
            val ballHeight = (viewPager.height - 3 * ScreenUtils.dip2px(10f))/2
            bottomViewWidth = 2 * (ballHeight + ScreenUtils.dip2px(15f))
            val params1 = leftView.layoutParams as RelativeLayout.LayoutParams
            params1.width = bottomViewWidth
            params1.height = bottomViewWidth
            params1.leftMargin = -(bottomViewWidth / 2)
            params1.bottomMargin = -(bottomViewWidth / 2)
            leftView.layoutParams = params1

            val params2 = rightView.layoutParams as RelativeLayout.LayoutParams
            params2.width = bottomViewWidth
            params2.height = bottomViewWidth
            params2.rightMargin = -(bottomViewWidth / 2)
            params2.bottomMargin = -(bottomViewWidth / 2)
            rightView.layoutParams = params2
        }
    }

    override fun update(id: Int, vararg args: Any?) {
        if (id == ObserverIds.COLORS_ITEM_SELECTED) {
            ColorHistoryFactory.put(ColorsItemFactory.leftColorsItem)
            addAnimator()
        }
    }

    private fun addAnimator() {
        val animator1 = ObjectAnimator.ofFloat(leftView, "scaleX", 1.0f, 0.0f)
        val animator2 = ObjectAnimator.ofFloat(leftView, "scaleY", 1.0f, 0.0f)
        val animator3 = ObjectAnimator.ofFloat(rightView, "scaleX", 1.0f, 0.0f)
        val animator4 = ObjectAnimator.ofFloat(rightView, "scaleY", 1.0f, 0.0f)
        val animator9 = ObjectAnimator.ofFloat(leftView, "alpha", 1.0f, 0.0f)
        val animator10 = ObjectAnimator.ofFloat(rightView, "alpha", 1.0f, 0.0f)
        val animatorSet1 = AnimatorSet()
        animatorSet1.duration = 300
        animatorSet1.interpolator = DecelerateInterpolator()
        animatorSet1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                setColors(ColorsItemFactory.leftColorsItem, leftView.image)
                setColors(ColorsItemFactory.rightsColorsItem, rightView.image)
                leftView?.show()
                rightView?.show()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        animatorSet1.playTogether(animator1, animator2, animator3, animator4, animator9, animator10)
        val animator5 = ObjectAnimator.ofFloat(leftView, "scaleX", 0.0f, 1.0f)
        val animator6 = ObjectAnimator.ofFloat(leftView, "scaleY", 0.0f, 1.0f)
        val animator7 = ObjectAnimator.ofFloat(rightView, "scaleX", 0.0f, 1.0f)
        val animator8 = ObjectAnimator.ofFloat(rightView, "scaleY", 0.0f, 1.0f)
        val animator11 = ObjectAnimator.ofFloat(leftView, "alpha", 0.0f, 1.0f)
        val animator12 = ObjectAnimator.ofFloat(rightView, "alpha", 0.0f, 1.0f)
        val animatorSet2 = AnimatorSet()
        animatorSet2.duration = 300
        animatorSet2.interpolator = AccelerateInterpolator()
        animatorSet2.playTogether(animator5, animator6, animator7, animator8, animator11, animator12)
        val animatorSet3 = AnimatorSet()
        animatorSet3.playSequentially(animatorSet1, animatorSet2)
        animatorSet3.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ObserverManger.getInstance().removeObserver(this)
        leftRotate.cancel()
        rightRotate.cancel()
    }

    fun addColorGuide(){
        NewbieGuide.with(context as Activity)
            .setLabel("color")
            .setShowCounts(1)
            .addGuidePage(
                GuidePage.newInstance()
                    .addHighLight(category)
                    .setLayoutRes(R.layout.view_guide_simple)
                    .setOnLayoutInflatedListener { view, controller ->
                        val img = view.findViewById<ImageView>(R.id.img)
                        img.setImageResource(R.drawable.aqhy_new_guide2)
                        val params = img.layoutParams as RelativeLayout.LayoutParams
                        params.bottomMargin = ScreenUtils.dip2px(160f)
                        params.leftMargin = ScreenUtils.dip2px(20f)
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        img.layoutParams = params
                    })
            .setOnGuideChangedListener(object : OnGuideChangedListener {
                override fun onRemoved(controller: Controller?) {
                    addHistoryGuide()
                }

                override fun onShowed(controller: Controller?) {
                    Log.i("guide", "show top")
                }

            })
            .show()
    }

    private fun addHistoryGuide(){
        NewbieGuide.with(context as Activity)
            .setLabel("history")
            .setShowCounts(1)
            .addGuidePage(
                GuidePage.newInstance()
                    .addHighLight(history)
                    .setLayoutRes(R.layout.view_guide_simple)
                    .setOnLayoutInflatedListener { view, controller ->
                        val img = view.findViewById<ImageView>(R.id.img)
                        img.setImageResource(R.drawable.aqhy_new_guide3)
                        val params = img.layoutParams as RelativeLayout.LayoutParams
                        params.bottomMargin = ScreenUtils.dip2px(160f)
                        params.rightMargin = ScreenUtils.dip2px(20f)
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                        img.layoutParams = params
                    })
            .setOnGuideChangedListener(object : OnGuideChangedListener {
                override fun onRemoved(controller: Controller?) {

                }

                override fun onShowed(controller: Controller?) {
                    Log.i("guide", "show top")
                }

            })
            .show()
    }
}