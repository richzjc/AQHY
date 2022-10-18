package com.micker.aqhy.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.micker.aqhy.R
import com.micker.aqhy.dialog.UserPrivacyDialog
import com.micker.aqhy.util.setMsgToWX
import com.micker.aqhy.widget.ViewPagerScroller
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.file.FileUtil
import com.micker.helper.file.QDUtil.getShareImageCache
import com.micker.helper.router.DoubleClickHelper
import com.micker.helper.snack.MToastHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.fragment.HomeFragment
import com.micker.home.fragment.InspirationFragment
import com.micker.user.fragment.UserFragment
import kotlinx.android.synthetic.main.tail_activity_main.*
import java.util.*

class MainActivity : BaseActivity<Any, BasePresenter<Any>>() {
    private val titles by lazy { arrayOf("首页", "灵感", "个人") }
    private val duration: Int = 800
    lateinit var mFragments: MutableList<Fragment>
    override fun doGetContentViewId() = R.layout.tail_activity_main
    override fun isNeedSwipeBack() = false
    override fun doInitSubViews(view: View) {
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
        animator1.addUpdateListener {
            tb.setTitleAlpha(it.animatedValue as Float)
        }
        animator1.duration = (duration / 2).toLong()
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                tb.title = titles[tabIndex]
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        val animator2 = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator2.addUpdateListener {
            tb.setTitleAlpha(it.animatedValue as Float)
        }
        animator2.duration = (duration / 2).toLong()
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2)
        animatorSet.start()
    }

    private fun initViewPager() {
        viewPager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = mFragments[position]
            override fun getCount() = mFragments.size
        }
        tb?.title = titles[1]
        viewPager?.currentItem = 1
        navigationView?.selectedItemId = R.id.menu_cart
        viewPager.offscreenPageLimit = 3
        val pagerScroller = ViewPagerScroller(this)
        pagerScroller.setScrollDuration(duration)
        pagerScroller.initViewPagerScroll(viewPager)
    }

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(HomeFragment())
        mFragments.add(InspirationFragment())
        mFragments.add(UserFragment())
    }

}