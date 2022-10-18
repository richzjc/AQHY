package com.micker.user.activity

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseRecyclerViewActivity
import com.micker.data.constant.USER_MAIN_SETTING_ACTIVITY
import com.micker.user.adapter.UserItemAdapter
import com.micker.user.callback.UserCallback
import com.micker.user.model.UserItemEntity
import com.micker.user.presenter.UserPresenter

@BindRouter(urls = [USER_MAIN_SETTING_ACTIVITY])
class SettingActivity : BaseRecyclerViewActivity<UserItemEntity, UserCallback, UserPresenter>(), UserCallback {

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        titleBar.title = "设置"
        ptrRecyclerView.setCanRefresh(false)
        recycleView.setIsEndless(false)
    }

    override fun onRefresh() {
    }

    override fun onLoadMore(page: Int) {
    }

    override fun doGetPresenter() = UserPresenter()
    override fun doInitAdapter() = UserItemAdapter()

    override fun doInitData() {
        super.doInitData()
        mPresenter?.loadSettingData()
    }
}