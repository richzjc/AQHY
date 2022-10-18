package com.micker.home.adapter

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.data.constant.POSTER_SHARE_ACTION
import com.micker.data.model.inspiration.InspirationEntity
import com.micker.helper.UtilsContextManager
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.micker.home.holder.InspirationViewHolder
import com.micker.home.holder.SvgPosterViewHolder
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_recycler_item_home_category_list.view.*

class InspirationAdapter(val isShowFindInspiration: Boolean) :
    BaseRecycleAdapter<InspirationEntity, InspirationViewHolder>() {
    private var downX: Int = 0
    private var downY: Int = 0
    private var screenWidth = 0
    private var screenHeight = 0

    init {
        screenHeight = UtilsContextManager.getInstance().application.resources.displayMetrics.heightPixels;
        screenWidth = UtilsContextManager.getInstance().application.resources.getDisplayMetrics().widthPixels;
    }

    override fun binderItemHolder(holder: InspirationViewHolder?, position: Int) {
        holder?.doBindData(get(position))
        holder?.itemView?.delete?.setTag(R.id.tag_entity, get(position))
        holder?.itemView?.delete?.setTag(R.id.tag_position, position)
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): InspirationViewHolder {
        val holder = InspirationViewHolder(parent?.context, isShowFindInspiration)
        holder?.itemView?.delete?.setOnClickListener {
            if (it.getTag(R.id.tag_isshare) == true) {
                val entity = it.getTag(R.id.tag_entity) as? InspirationEntity
                val bundle = Bundle()
                bundle.putString("holder", SvgPosterViewHolder::class.java.name)
                bundle.putParcelable("parcel", entity)
                RouterHelper.open(POSTER_SHARE_ACTION, it.context, bundle)
            } else {
                showPopupwindow(it)
            }
        }

        holder?.itemView?.delete?.setOnTouchListener { v, event ->
            downX = event.rawX.toInt()
            downY = event.rawY.toInt()
            false
        }
        return holder
    }


    private fun showPopupwindow(view: View) {
        val entity = view.getTag(R.id.tag_entity) as? InspirationEntity
        val position = view.getTag(R.id.tag_position) as Int
        val contentView = LayoutInflater.from(view.context).inflate(R.layout.aqhy_dialog_inspiration_more, null)
        var mPopupWindow: PopupWindow? = null
        val listener = View.OnClickListener {
            val tag = it.tag
            when (tag) {
                "分享" -> {
                    val bundle = Bundle()
                    bundle.putString("holder", SvgPosterViewHolder::class.java.name)
                    bundle.putParcelable("parcel", entity)
                    RouterHelper.open(POSTER_SHARE_ACTION, it.context, bundle)
                }
                "删除" -> {
                    if (adapterItemClickListener != null) {
                        it.tag = "删除"
                        adapterItemClickListener.onViewClick(it, entity, position)
                    }
                }
                "发布" -> {
                    if (adapterItemClickListener != null) {
                        it.tag = "发布"
                        adapterItemClickListener.onViewClick(it, entity, position)
                    }
                }
            }
            mPopupWindow?.dismiss()
        }
        val view1 = contentView.findViewById<AppCompatTextView>(R.id.menu_item1)
        val view2 = contentView.findViewById<AppCompatTextView>(R.id.menu_item2)
        val view3 = contentView.findViewById<AppCompatTextView>(R.id.menu_item3)
        view1.setOnClickListener(listener)
        view2.setOnClickListener(listener)
        view3.setOnClickListener(listener)
        if (entity?.state == 1) {
            view3?.hide(true)
            view1.text = "分享"
            view1.tag = "分享"
            view2.text = "删除"
            view2.tag = "删除"
        } else {
            view3?.show()
            view1.text = "分享"
            view1.tag = "分享"
            view2.text = "删除"
            view2.tag = "删除"
            view3.text = "发布"
            view3.tag = "发布"
        }

        mPopupWindow = PopupWindow(contentView, ScreenUtils.dip2px(110f), ViewGroup.LayoutParams.WRAP_CONTENT, true)
        mPopupWindow.setBackgroundDrawable(ColorDrawable())
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popuHeight = contentView.measuredHeight
        var heightY: Int
        if (downY > screenHeight / 2) {
            heightY = downY - popuHeight
        } else {
            heightY = downY + ScreenUtils.dip2px(10f)
        }
        mPopupWindow.showAtLocation(view, Gravity.TOP or Gravity.START, downX - ScreenUtils.dip2px(10f), heightY)
    }
}