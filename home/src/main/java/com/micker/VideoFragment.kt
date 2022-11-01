package com.micker

import androidx.fragment.app.Fragment
import com.micker.core.adapter.BaseFragmentAdapter
import com.micker.core.base.BaseFragment
import com.micker.core.base.BasePresenter
import com.micker.five.activity.FiveStageFragment
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_fragment_main_tablayout.*
import java.util.*

class VideoFragment : BaseFragment<Any, BasePresenter<Any>>() {

    private val titles by lazy { arrayOf("家庭教育", "安全知识", "百科知识", "经典语录", "励志语录") }
    lateinit var mFragments: MutableList<Fragment>

    override fun doGetContentViewId(): Int {
        return R.layout.aqhy_fragment_main_tablayout
    }

    override fun doInitData() {
        super.doInitData()
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
        mFragments.add(FiveStageFragment())
        mFragments.add(FiveStageFragment())
        mFragments.add(FiveStageFragment())
        mFragments.add(FiveStageFragment())
        mFragments.add(FiveStageFragment())
    }
}