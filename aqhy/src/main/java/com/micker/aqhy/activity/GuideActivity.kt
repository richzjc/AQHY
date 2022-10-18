package com.micker.aqhy.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.kronos.router.BindRouter
import com.micker.aqhy.R
import com.micker.aqhy.adapter.GuideAdapter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.data.constant.GUIDEACTIVITY_ACTION
import com.micker.global.indicator.IndicatorView
import com.micker.global.indicator.ViewPagerHelper
import com.micker.helper.ResourceUtils
import com.micker.helper.router.ActivityHelper
import com.micker.helper.system.WindowHelper
import kotlinx.android.synthetic.main.aqhy_activity_guide.*


@BindRouter(urls = arrayOf(GUIDEACTIVITY_ACTION))
class GuideActivity : BaseActivity<Any, BasePresenter<Any>>(), ViewPager.OnPageChangeListener{

    private var adapter: GuideAdapter? = null

    override fun doGetContentViewId() = R.layout.aqhy_activity_guide

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowHelper.toggleLightStatusBar(this, true)
        super.onCreate(savedInstanceState)
    }

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        val indicatorView = IndicatorView(this)
        val color = Color.parseColor("#999999")
        indicatorView.setNormalColor(Color.parseColor("#e6e6e6"))
        indicatorView.setSelectedColor(color)
        magic!!.navigator = indicatorView
        ViewPagerHelper.bind(magic, viewpager!!)
        val guideImgs = guideImgs(this)
        indicatorView.setCount(guideImgs.size)

        adapter = GuideAdapter(listOf(*guideImgs))
        viewpager!!.adapter = adapter
        viewpager!!.addOnPageChangeListener(this)
        setFocusIndicatorIndex(0)

        tv_jump.setOnClickListener { responseToJump() }
    }

    override fun isNeedSwipeBack() = false

    private fun setFocusIndicatorIndex(index: Int) {
        if (index == adapter!!.count - 1) {
            tv_jump!!.visibility = View.GONE
        } else {
            tv_jump!!.visibility = View.VISIBLE
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) = setFocusIndicatorIndex(position)


    override fun onPageScrollStateChanged(state: Int) {}

    fun responseToJump() {
        val bundle = intent.extras
        if (bundle != null) {
            val isFromDeclare = bundle.getBoolean("isFromDeclare", false)
            if (isFromDeclare)
                finish()
            else
                toMain()
        } else {
            toMain()
        }
    }

    private fun toMain() {
        ActivityHelper.startActivity(this, MainActivity::class.java)
        finish()
    }

    companion object {
        @JvmStatic
        fun guideImgs(context: Context):Array<Int>  {
            var array = ResourceUtils.getResStringArrayFromId(R.array.guide_images)
            return Array(array.size, init = {
                ResourceUtils.getDrawableId(context, array[it])
            })
        }
    }
}
