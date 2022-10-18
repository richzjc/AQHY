package com.micker.user.fragment

import android.view.View
import com.micker.core.base.BaseRecyclerViewFragment
import com.micker.data.constant.ACCOUNT_STATE_CHANGED
import com.micker.helper.observer.Observer
import com.micker.helper.observer.ObserverManger
import com.micker.user.R
import com.micker.user.adapter.UserItemAdapter
import com.micker.user.callback.UserCallback
import com.micker.user.model.UserItemEntity
import com.micker.user.presenter.UserPresenter
import com.micker.user.widget.UserHeaderView
import com.pawegio.kandroid.hide
import kotlinx.android.synthetic.main.aqhy_fragment_user.*

class UserFragment : BaseRecyclerViewFragment<UserItemEntity, UserCallback, UserPresenter>(), UserCallback, Observer {

    val headerView by lazy { UserHeaderView(ptrRecyclerView) }
    override fun doGetContentViewId() = R.layout.aqhy_fragment_user

    override fun doInitSubViews(view: View?) {
        super.doInitSubViews(view)
        ObserverManger.getInstance().registerObserver(this, ACCOUNT_STATE_CHANGED)
        recycleView?.setIsEndless(false)
    }

    override fun doInitData() {
        super.doInitData()
        titlebar.title = "个人"
        titlebar.hide(true)
        ptrRecyclerView.setCanRefresh(false)
        adapter.addHeader(headerView.view)
        mPresenter.loadData()
    }

    override fun doGetPresenter() = UserPresenter()

    override fun onLoadMore(page: Int) {
    }

    override fun onRefresh() {
    }

    override fun update(id: Int, vararg args: Any?) {
        headerView?.update()
        mPresenter?.loadData()
    }

    override fun doInitAdapter() = UserItemAdapter()

    override fun onDestroyView() {
        super.onDestroyView()
        ObserverManger.getInstance().removeObserver(this)
    }
}