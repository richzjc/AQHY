package com.micker.home.fragment

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.micker.core.base.BaseRecyclerViewFragment
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.WscnImageView
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.home.HomeEntity
import com.micker.helper.router.RouterHelper
import com.micker.home.R
import com.micker.home.adapter.HomeAdapter
import com.micker.home.callback.HomeCallback
import com.micker.home.presenter.HomePresenter
import com.micker.home.widget.MyViewFlipper
import com.pawegio.kandroid.hide
import kotlinx.android.synthetic.main.aqhy_fragment_main.*
import kotlinx.android.synthetic.main.aqhy_view_header_home_inspire.view.*

class HomeFragment : BaseRecyclerViewFragment<HomeEntity, HomeCallback, HomePresenter>(),
    HomeCallback {

    override fun onLoadMore(page: Int) {
        mPresenter.loadData(false)
    }

    override fun onRefresh() {
        mPresenter.loadData(true)
    }

    override fun doInitAdapter() = HomeAdapter()

    override fun doGetContentViewId() = R.layout.aqhy_fragment_main

    override fun doGetPresenter() = HomePresenter()
    override fun doInitData() {
        super.doInitData()
        titlebar.title = "首页"
        titlebar.hide(true)
        ptrRecyclerView.setCanRefresh(false)
        recycleView?.start()
        mPresenter?.loadData(true)
        mPresenter?.loadBaner()
    }

    override fun setData(results: MutableList<HomeEntity>?, isCache: Boolean) {
        super.setData(results, isCache)
        recycleView.changeToMerginState()
    }

    override fun requestSucc(model: List<LaunchConfigEntity>?) {
        model?.also {
            if (it.isNotEmpty()) {
                val headerView = LayoutInflater.from(context)
                    .inflate(R.layout.aqhy_view_header_home_inspire, ptrRecyclerView, false)
                headerView?.flipper?.overScrollMode = View.OVER_SCROLL_NEVER
                headerView?.flipper?.setListSize(it.size)
                headerView?.flipper?.setOnPageChangeListener(object :
                    MyViewFlipper.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Float
                    ) {
                        val view = headerView.flipper.getOtherPosterView() as WscnImageView
                        if (positionOffsetPixels > 0) {
                            val previous = headerView.flipper.previousItem
                            view.tag = it[previous]
                            ImageLoadManager.loadImage(
                                it[previous].image,
                                view,
                                0
                            )
                        } else {
                            val next = headerView.flipper.nextItem
                            view.tag = it[next]
                            ImageLoadManager.loadImage(
                                it[next].image,
                                view,
                                0
                            )
                        }
                    }

                    override fun onPageSelected(position: Int, isNext: Boolean) {
                        headerView?.count?.text = "${position + 1}/${it.size}"
                        headerView?.desc?.text = it[position]?.desc
                    }
                })
                val defaultIndex = 0
                val imageView1 = WscnImageView(context)
                val imageView2 = WscnImageView(context)

                ImageLoadManager.loadImage(it[defaultIndex].image, imageView1, 0)
                ImageLoadManager.loadImage(it[defaultIndex].image, imageView2, 0)

                imageView1.tag = it[defaultIndex]
                imageView2.tag = it[defaultIndex]

                headerView?.flipper?.addView(imageView1)
                headerView?.flipper?.addView(imageView2)

                headerView?.count?.text = "${defaultIndex + 1}/${it.size}"
                headerView?.desc?.text = it[defaultIndex]?.desc

                imageView1.setOnClickListener {
                    val tag = it.tag as LaunchConfigEntity
                    RouterHelper.open(tag.url, it.context)
                }

                imageView2.setOnClickListener {
                    val tag = it.tag as LaunchConfigEntity
                    RouterHelper.open(tag.url, it.context)
                }
                adapter?.addHeader(headerView)
            }
        }
    }

    override fun onResponseError(code: Int) {
        super.onResponseError(code)
        recycleView.changeToMerginState()
    }
}