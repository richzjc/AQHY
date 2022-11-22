package com.micker

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.micker.core.adapter.BaseFragmentAdapter
import com.micker.core.base.BaseFragment
import com.micker.core.base.BasePresenter
import com.micker.five.activity.FiveStageFragment
import com.micker.helper.Util
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_fragment_main_tablayout.*
import java.util.*

class VideoFragment : BaseFragment<Any, BasePresenter<Any>>() {

    private val titles by lazy { arrayOf("家庭教育", "励志语录") }
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
        val fragment1 = FiveStageFragment()
        val bundle1 = Bundle()
        bundle1.putString("apiPath", "http://yuanyuan0914.top/config/home_education.json")
        fragment1.arguments = bundle1
        mFragments.add(fragment1)

        val fragment5 = FiveStageFragment()
        val bundle5 = Bundle()
        bundle5.putString("apiPath", "http://yuanyuan0914.top/config/video_lizhi.json")
        fragment5.arguments = bundle5
        mFragments.add(fragment5)
    }
}