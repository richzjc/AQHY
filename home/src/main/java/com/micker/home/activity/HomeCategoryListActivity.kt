package com.micker.home.activity

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.data.constant.HOME_CATEGORY_LIST_ACTIVITY
import com.micker.helper.FragmentHelper
import com.micker.home.R
import com.micker.home.fragment.HomeCategoryListFragment

@BindRouter(urls = [HOME_CATEGORY_LIST_ACTIVITY])
class HomeCategoryListActivity : BaseActivity<Any, BasePresenter<Any>>() {
    override fun doGetContentViewId() = R.layout.aqhy_activity_home_category_list

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        val fragment = HomeCategoryListFragment()
        val bundle = intent.extras
        fragment.arguments = bundle
        FragmentHelper.addFragment(supportFragmentManager, R.id.content, fragment, false)
    }
}