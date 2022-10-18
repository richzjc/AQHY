package com.micker.home.activity

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.data.constant.USER_MY_WORKS
import com.micker.helper.FragmentHelper
import com.micker.home.R
import com.micker.home.fragment.UserMyWorksFragment

@BindRouter(urls = [USER_MY_WORKS])
class UserWorksActivity : BaseActivity<Any, BasePresenter<Any>>() {
    override fun doGetContentViewId() = R.layout.aqhy_activity_home_category_list

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        val fragment = UserMyWorksFragment()
        val bundle = intent.extras
        fragment.arguments = bundle
        FragmentHelper.addFragment(supportFragmentManager, R.id.content, fragment, false)
    }
}