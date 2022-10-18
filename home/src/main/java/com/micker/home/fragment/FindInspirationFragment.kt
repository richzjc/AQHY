package com.micker.home.fragment

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import com.micker.core.base.BaseWaterfallFragment
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.constant.SVG_DETAIL_ACTIVITY
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.helper.Util
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.adapter.InspirationAdapter
import com.micker.home.callback.InspirationCallback
import com.micker.home.presenter.FindInspirationPresenter
import kotlinx.android.synthetic.main.aqhy_fragment_find_inspiration_list.*

class FindInspirationFragment :
    BaseWaterfallFragment<InspirationEntity, InspirationCallback, FindInspirationPresenter>(), InspirationCallback {

    private val entity by lazy { arguments?.getParcelable<HomeSubItemEntity>("entity") }
    private val svgId by lazy { arguments?.getString("svgId", "") }

    override fun onLoadMore(page: Int) {
        mPresenter.loadData(false, svgId)
    }

    override fun onRefresh() {
        mPresenter.loadData(true, svgId)
    }

    override fun doGetContentViewId() = R.layout.aqhy_fragment_find_inspiration_list

    override fun doGetPresenter() = FindInspirationPresenter()

    override fun doInitAdapter() = InspirationAdapter(false)

    override fun doInitData() {
        super.doInitData()
        content?.setPadding(0, Util.getStatusBarHeight(context), 0, 0)
        setListener()
        initImage()
        mPresenter?.loadData(true, svgId)
    }

    private fun initImage() {
        entity?.also {
            try {
                val params = image.layoutParams as? FrameLayout.LayoutParams
                val height = ScreenUtils.dip2px(35f)
                val width = (height * it.imgWidth) / it.imgHeight
                params?.width = width
                params?.height = height
                ImageLoadManager.loadImage(it.image, image, 0)
                content?.setBackgroundColor(Color.WHITE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setListener() {
        adapter?.setAdapterItemClickListener { view, entity, position ->
            if (view.tag == "删除")
                mPresenter?.delete(entity as? InspirationEntity)
            else if(view.tag == "发布"){
                val bundle = Bundle()
                bundle.putString("contentUrl", (entity as InspirationEntity).svgUrl)
                bundle.putString("imageUrl", entity.image)
                bundle.putParcelable("entity", entity.getNewSubItemEntity())
                mPresenter?.updateContent(bundle, 1, svgId)
            }
        }

        icon_back?.setOnClickListener { (context as? Activity)?.finish() }
        paint?.setOnClickListener {
            entity?.apply {
                val bundle = Bundle()
                bundle.putParcelable("entity", entity)
                RouterHelper.open(SVG_DETAIL_ACTIVITY, context, bundle)
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