package com.micker.home.fragment

import android.view.View
import com.micker.core.base.BaseWaterfallFragment
import com.micker.data.model.home.HomeEntity
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.home.adapter.HomeCategoryListAdapter
import com.micker.home.callback.HomeCategoryListCallback
import com.micker.home.presenter.HomeCategoryListPresenter

class HomeCategoryListFragment : BaseWaterfallFragment<HomeSubItemEntity, HomeCategoryListCallback, HomeCategoryListPresenter>(){

    private val groupId by lazy {
        val entity = arguments?.getParcelable<HomeEntity>("entity")
        entity?.id ?: ""
    }
    override fun onLoadMore(page: Int) {
        mPresenter?.loadData(groupId, false)
    }

    override fun onRefresh() {
        mPresenter?.loadData(groupId, true)
    }

    override fun doGetPresenter() = HomeCategoryListPresenter()

    override fun doInitAdapter() = HomeCategoryListAdapter()

    override fun doInitSubViews(view: View?) {
        super.doInitSubViews(view)
        val entity = arguments?.getParcelable<HomeEntity>("entity")
        titleBar?.title = entity?.name
    }
    override fun doInitData() {
        super.doInitData()
        mPresenter.loadData(groupId, true)
    }
}