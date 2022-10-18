package com.micker.home.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorGroupDetailEntity
import com.micker.helper.router.RouterHelper
import com.micker.home.R
import com.micker.home.activity.SvgDetailActivity
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_view_svg_detail_footer.view.*

class SvgDetailFooterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var colorCategoryList: ArrayList<ColorCategoryEntity>? = null

    init {
        setBackgroundColor(Color.WHITE)
        LayoutInflater.from(getContext()).inflate(R.layout.aqhy_view_svg_detail_footer, this, true)
    }

    fun loadColorCategorySuccess(list: ArrayList<ColorCategoryEntity>) {
        this.colorCategoryList = list
    }

    fun historyViewShowCategoryData() {
        history_view?.bindColorCategoryData(colorCategoryList)
    }

    fun historyViewShowHistoryData() {
        history_view?.bindHistoryData()
    }

    fun showHistoryView() {
        val animator1 = ObjectAnimator.ofFloat(history_view, "alpha", 0f, 1f)
        val animator2 = ObjectAnimator.ofFloat(outer_view, "alpha", 1f, 0f)
        animator1.duration = 400
        animator2.duration = 400
        animator2.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                outer_view?.hide(true)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                history_view?.show()
            }
        })
        val set = AnimatorSet()
        set.playTogether(animator1, animator2)
        set.start()
    }

    fun showOuterView() {
        val animator1 = ObjectAnimator.ofFloat(history_view, "alpha", 1f, 0f)
        val animator2 = ObjectAnimator.ofFloat(outer_view, "alpha", 0f, 1f)
        animator1.duration = 200
        animator2.duration = 200
        animator2.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                history_view?.hide(true)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                outer_view?.show()
            }
        })
        val set = AnimatorSet()
        set.playTogether(animator1, animator2)
        set.start()
    }

    fun updateCategoryData(type: Int) {
        val activity = RouterHelper.getActivity(context)
        (activity as? SvgDetailActivity)?.apply {
            mPresenter?.loadColorListByCategory(type)
        }
    }

    fun loadColorListSuccess(entity: ColorGroupDetailEntity?) {
        outer_view?.bindData(entity)
    }

    fun addBottomGuide(){
        outer_view?.addColorGuide()
    }
}