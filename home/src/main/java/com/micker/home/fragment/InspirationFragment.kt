package com.micker.home.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.micker.core.base.BaseWaterfallFragment
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.WscnImageView
import com.micker.data.constant.ACCOUNT_STATE_CHANGED
import com.micker.data.model.aqhy.LaunchConfigEntity
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.helper.observer.Observer
import com.micker.helper.observer.ObserverManger
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.adapter.InspirationAdapter
import com.micker.home.callback.InspirationCallback
import com.micker.home.presenter.InspirationPresenter
import com.micker.home.widget.MyViewFlipper
import com.pawegio.kandroid.hide
import kotlinx.android.synthetic.main.aqhy_fragment_main.*
import kotlinx.android.synthetic.main.aqhy_view_header_home_inspire.view.*

class InspirationFragment :
    BaseWaterfallFragment<InspirationEntity, InspirationCallback, InspirationPresenter>(),
    InspirationCallback, Observer {
    private var headerView: View? = null
    override fun onLoadMore(page: Int) {
        mPresenter.loadData(false)
    }

    override fun onRefresh() {
        mPresenter.loadData(true)
    }

    override fun doInitAdapter() = InspirationAdapter(true)


    override fun doGetPresenter() = InspirationPresenter()

    override fun doInitData() {
        super.doInitData()
        titleBar.title = "灵感"
        titlebar.hide(true)
        adapter?.setData(null)
        titleBar.setIconBackVisible(View.INVISIBLE)
        mPresenter.loadData(true)
        mPresenter.loadBanner()
    }

    override fun addItemDecoration() {
        recycleView.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (view == headerView) {
                    outRect.top = 0
                    outRect.bottom = ScreenUtils.dip2px(10f)
                    outRect.left = 0
                    outRect.right = 0
                } else {
                    val layoutParams =
                        view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                    val spanIndex = layoutParams.spanIndex
                    outRect.top = 0
                    outRect.bottom = ScreenUtils.dip2px(10f)
                    if (spanIndex == 0) {
                        outRect.left = ScreenUtils.dip2px(10f)
                        outRect.right = ScreenUtils.dip2px(10f)
                    } else {
                        outRect.right = ScreenUtils.dip2px(10f)
                        outRect.left = 0
                    }
                }
            }
        })
    }

    override fun doInitSubViews(view: View?) {
        super.doInitSubViews(view)
        ObserverManger.getInstance().registerObserver(this, ACCOUNT_STATE_CHANGED)
        adapter?.setAdapterItemClickListener { view, entity, position ->
            if (view.tag == "删除")
                mPresenter?.delete(entity as? InspirationEntity)
            else if (view.tag == "发布") {
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
        model?.also {
            if (it.isNotEmpty()) {
                (ptrRecyclerView.layoutParams as? ViewGroup.MarginLayoutParams)?.also {
                    it.topMargin = 0
                }

                headerView = LayoutInflater.from(context)
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
                        val view = headerView?.flipper?.getOtherPosterView() as WscnImageView
                        if (positionOffsetPixels > 0) {
                            val previous = headerView?.flipper?.previousItem ?: 0
                            view.tag = it[previous]
                            ImageLoadManager.loadImage(
                                it[previous].image,
                                view,
                                0
                            )
                        } else {
                            val next = headerView?.flipper?.nextItem ?: 0
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

                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                headerView?.flipper?.addView(imageView1, params)
                headerView?.flipper?.addView(imageView2, params)

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

    override fun update(id: Int, vararg args: Any?) {
        when (id == ACCOUNT_STATE_CHANGED) {
            true -> ptrRecyclerView.autoRefresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ObserverManger.getInstance().removeObserver(this)
    }
}