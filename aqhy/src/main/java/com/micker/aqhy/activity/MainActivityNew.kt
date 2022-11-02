package com.micker.aqhy.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.micker.NewsMainFragment
import com.micker.VideoFragment
import com.micker.aqhy.R
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.helper.ResourceUtils
import com.micker.helper.Util
import com.micker.helper.file.FileUtil
import com.micker.helper.file.QDUtil.getShareImageCache
import com.micker.helper.router.DoubleClickHelper
import com.micker.helper.snack.MToastHelper
import com.micker.helper.speak.TextToSpeechUtils.initTextToSpeech
import com.micker.helper.speak.TextToSpeechUtils.textSpeechclose
import com.micker.helper.system.ScreenUtils
import com.micker.home.widget.ViewPagerScroller
import com.micker.user.fragment.UserFragment
import kotlinx.android.synthetic.main.tail_activity_main_new1.*
import java.util.ArrayList

class MainActivityNew : BaseActivity<Any, BasePresenter<Any>>() {

    lateinit var mFragments: MutableList<Fragment>
    private val duration: Int = 800
    override fun doGetContentViewId() = R.layout.tail_activity_main_new1
    override fun isNeedSwipeBack() = false

    override fun doInitSubViews(view: View) {
        initTextToSpeech(application)
        initFragments()
        initViewPager()
        initNavigation()
        navigationView?.itemIconSize = ScreenUtils.dip2px(20f)
    }

    private fun initNavigation() {
        navigationView?.setOnNavigationItemSelectedListener { item ->
            var tab = 0
            when (item.itemId) {
                R.id.menu_home -> tab = 0
                R.id.menu_cart -> tab = 1
                R.id.menu_about -> tab = 2
            }
            addAnimation(tab)
            viewPager?.currentItem = tab
            val menuItem = navigationView.menu.getItem(tab)
            menuItem.isChecked = true;
            false
        }
    }

    private fun addAnimation(tabIndex: Int) {
        if (viewPager.currentItem == tabIndex)
            return
        val animator1 = ValueAnimator.ofFloat(1.0f, 0.0f)
        animator1.duration = (duration / 2).toLong()
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        val animator2 = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator2.duration = (duration / 2).toLong()
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2)
        animatorSet.start()
    }

    private fun initViewPager() {
        viewPager.adapter = object :
            FragmentPagerAdapter(
                supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
            override fun getItem(position: Int) = mFragments[position]
            override fun getCount() = mFragments.size
        }
        viewPager?.currentItem = 0
        navigationView?.selectedItemId = R.id.menu_home
        viewPager.offscreenPageLimit = 3
        val pagerScroller = ViewPagerScroller(this)
        pagerScroller.setScrollDuration(duration)
        pagerScroller.initViewPagerScroll(viewPager)
    }

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(NewsMainFragment())
        mFragments.add(VideoFragment())
        mFragments.add(UserFragment())
    }


    override fun onBackPressed() {
        when {
            !DoubleClickHelper.checkExitDoubleClick() -> {
                super.onBackPressed()
            }
            else -> MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.tail_exit_app_text))
        }
    }

    override fun onDestroy() {
        FileUtil.deleteDirectory(getShareImageCache(this))
        textSpeechclose()
        super.onDestroy()
    }
//
//    private fun initListener(){
//        first_stage?.setOnClickListener { RouterHelper.open(FIRST_STAGE_ROUTER, this) }
//        second_stage?.setOnClickListener { RouterHelper.open(SECOND_STAGE_ROUTER, this) }
//        third_stage?.setOnClickListener { RouterHelper.open(THIRD_STAGE_ROUTER, this) }
//        four_stage?.setOnClickListener { RouterHelper.open(FOUR_STAGE_ROUTER, this) }
//        five_stage?.setOnClickListener { RouterHelper.open(FIVE_STAGE_ROUTER, this) }
//    }
}