package com.micker

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.micker.child.NewsMainChildFragment
import com.micker.child.dialog.IntroduceDialog
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
        setListener()
    }

    private fun setListener(){
        imgChannel?.setOnClickListener {
            val dialog = IntroduceDialog()
            dialog.show(childFragmentManager, "introduce")
        }
    }

    private fun initViewPager() {
        val fragmentAdapter = BaseFragmentAdapter<Fragment>(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        fragmentAdapter.configData(Arrays.asList(*titles), mFragments)
    }

    private fun initFragments() {
        mFragments = ArrayList()
        val fragment1 = NewsMainChildFragment()
        val bundle1 = Bundle()
        bundle1.putString("json", "chinese.json")
        fragment1.arguments = bundle1
        mFragments.add(fragment1)

        val fragment2 = NewsMainChildFragment()
        val bundle2 = Bundle()
        bundle2.putString("json", "math.json")
        fragment2.arguments = bundle2
        mFragments.add(fragment2)

        val fragment3 = NewsMainChildFragment()
        val bundle3 = Bundle()
        bundle3.putString("json", "english.json")
        fragment3.arguments = bundle3
        mFragments.add(fragment3)

        val fragment4 = NewsMainChildFragment()
        val bundle4 = Bundle()
        bundle4.putString("json", "other.json")
        fragment4.arguments = bundle4
        mFragments.add(fragment4)
    }
}