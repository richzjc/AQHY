package com.micker

import com.micker.core.base.BaseFragment
import com.micker.core.base.BasePresenter
import com.micker.home.R

class NewsMainChildFragment : BaseFragment<Any, BasePresenter<Any>>() {
    override fun doGetContentViewId(): Int = R.layout.aqhy_fragment_main
}