package com.micker.home.activity

import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.kronos.router.BindRouter
import com.micker.core.adapter.BaseFragmentAdapter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.global.SECOND_STAGE_ROUTER
import com.micker.helper.Util
import com.micker.home.R
import com.micker.home.fragment.HomeFragment
import com.micker.home.fragment.InspirationFragment
import com.richzjc.library.TabLayout
import kotlinx.android.synthetic.main.aqhy_fragment_main_tablayout.*
import java.util.*

@BindRouter(urls = [SECOND_STAGE_ROUTER])
class MainActivity : BaseActivity<Any, BasePresenter<Any>>() {
    private val titles by lazy { arrayOf("首页", "灵感") }
    lateinit var mFragments: MutableList<Fragment>
    override fun doGetContentViewId() = R.layout.aqhy_fragment_main_tablayout
    override fun doInitSubViews(view: View) {
        parent_ll?.setPadding(0, Util.getStatusBarHeight(this), 0, 0)
        tabLayout?.tabGravity = TabLayout.GRAVITY_CENTER
        tabLayout?.tabMode = TabLayout.MODE_FIXED
        initFragments()
        initViewPager()
    }


    private fun initViewPager() {
        val fragmentAdapter = BaseFragmentAdapter<Fragment>(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        fragmentAdapter.configData(Arrays.asList(*titles), mFragments)
    }

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(HomeFragment())
        mFragments.add(InspirationFragment())
    }

}