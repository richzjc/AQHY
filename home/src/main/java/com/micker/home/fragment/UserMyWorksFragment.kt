package com.micker.home.fragment

import android.os.Bundle
import android.view.View
import com.micker.core.base.BaseWaterfallFragment
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.home.adapter.InspirationAdapter
import com.micker.home.callback.InspirationCallback
import com.micker.home.presenter.UserMyWorksPresenter

class UserMyWorksFragment : BaseWaterfallFragment<InspirationEntity, InspirationCallback, UserMyWorksPresenter>(),
    InspirationCallback {

    override fun onLoadMore(page: Int) {
        mPresenter.loadData(false)
    }

    override fun onRefresh() {
        mPresenter.loadData(true)
    }

    override fun doGetPresenter() = UserMyWorksPresenter()

    override fun doInitAdapter() = InspirationAdapter(true)

    override fun doInitData() {
        super.doInitData()
        mPresenter.loadData(true)
    }

    override fun doInitSubViews(view: View?) {
        super.doInitSubViews(view)
        titleBar?.title = "我的创作"
        adapter?.setAdapterItemClickListener { view, entity, position ->
            if (view.tag == "删除")
                mPresenter?.delete(entity as? InspirationEntity)
            else if(view.tag == "发布"){
                val bundle = Bundle()
                bundle.putString("contentUrl", (entity as InspirationEntity).svgUrl)
                bundle.putString("imageUrl", entity.image)
                bundle.putParcelable("entity", entity.getNewSubItemEntity())
                mPresenter?.updateContent(bundle, 1)
            }
        }
    }

    override fun deleteSuccess(entity: InspirationEntity) {
        val position = adapter.get().indexOf(entity)
        if (position >= 0) {
            adapter.get().removeAt(position)
            adapter.notifyItemChanged()
        }
    }

    override fun requestSucc(model: List<LaunchConfigEntity>?) {

    }

}