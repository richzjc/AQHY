package com.micker

import androidx.fragment.app.Fragment
import com.micker.child.NewsMainChildFragment
import com.micker.core.adapter.BaseFragmentAdapter
import com.micker.core.base.BaseFragment
import com.micker.core.base.BasePresenter
import com.micker.helper.Util
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_fragment_main_tablayout.*
import java.util.*

class NewsMainFragment : BaseFragment<Any, BasePresenter<Any>>(){

    private val titles by lazy { arrayOf("语文", "数学", "英语", "其它") }
    lateinit var mFragments: MutableList<Fragment>
    override fun doGetContentViewId(): Int {
        return R.layout.aqhy_fragment_main_tablayout
    }

    override fun doInitData() {
        super.doInitData()
        parent_ll?.setPadding(0, Util.getStatusBarHeight(context), 0, 0)
        initFragments()
        initViewPager()
    }

    private fun initViewPager() {
        val fragmentAdapter = BaseFragmentAdapter<Fragment>(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        fragmentAdapter.configData(Arrays.asList(*titles), mFragments)
    }

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(NewsMainChildFragment())
        mFragments.add(NewsMainChildFragment())
        mFragments.add(NewsMainChildFragment())
        mFragments.add(NewsMainChildFragment())
    }
}