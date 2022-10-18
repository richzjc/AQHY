package com.micker.home.holder

import android.content.Context
import android.os.Bundle
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.data.constant.HOME_CATEGORY_LIST_ACTIVITY
import com.micker.data.model.home.HomeEntity
import com.micker.helper.router.RouterHelper
import com.micker.home.R
import com.micker.home.adapter.HomeSubAdapter
import com.micker.home.widget.HorizontalRecycleView
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_recycler_item_home.view.*

class HomeViewHolder(context : Context?) : BaseRecycleViewHolder<HomeEntity>(context) {
   private val adapter by lazy { HomeSubAdapter() }
    init {
        itemView.rv.adapter = adapter
        itemView?.category_parent?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("entity", content)
            RouterHelper.open(HOME_CATEGORY_LIST_ACTIVITY, mContext, bundle)
        }

        itemView?.rv?.callback = object : HorizontalRecycleView.SwitchToNextCallback{
            override fun swtichToNext() {
                val bundle = Bundle()
                bundle.putParcelable("entity", content)
                RouterHelper.open(HOME_CATEGORY_LIST_ACTIVITY, mContext, bundle)
            }
        }
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_home

    override fun doBindData(content: HomeEntity?) {
        this.content = content
        itemView?.left_title?.text = content?.name

        if(content?.items != null && content!!.items!!.isNotEmpty()){
            itemView?.rv?.show()
            adapter?.setData(content.items)
        }else{
            itemView?.rv?.hide(true)
        }
    }
}