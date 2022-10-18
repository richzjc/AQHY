package com.micker.home.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.micker.core.imageloader.ImageLoadManager
import com.micker.core.imageloader.WscnImageView
import com.micker.data.model.home.ColorTypeEntity
import com.micker.data.model.home.ColorsItem
import com.micker.home.R
import com.micker.home.utils.setColors
import com.micker.home.widget.list.Adapter
import kotlinx.android.synthetic.main.aqhy_recycler_view_color_list_net_image.view.*

class ColorListAdapter : Adapter {

    private var mDatas: List<ColorsItem>? = null
    var itemClickCallback: ItemClickCallback? = null
    fun setData(datas: List<ColorsItem>?) {
        if (datas != null && datas.size > 13) {
            val list = ArrayList<ColorsItem>()
            list.addAll(datas.subList(0, 13))
            this.mDatas = list
        } else {
            this.mDatas = datas
        }
    }

    override fun getItemCount(): Int {
        return mDatas?.size ?: 0
    }

    override fun onCreateViewHodler(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aqhy_recycler_view_color_list_net_image, parent, false)
        view.setOnClickListener(ItemClickListener(itemClickCallback))
        return view
    }

    override fun onBinderViewHodler(position: Int, convertView: View, parent: ViewGroup) {
        convertView.tag = mDatas!![position]
        if (getItemViewType(position) == SOLIDCOLOR) {
            setColors(mDatas!![position], convertView.image)
        } else {
            ImageLoadManager.loadImage(mDatas!![position].image, convertView.image as WscnImageView, 0)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val entity = mDatas!![position]
        return when (entity.type) {
            ColorTypeEntity.MaterialTypeSolidColor.type -> SOLIDCOLOR
            ColorTypeEntity.MaterialTypeLinearColor.type -> SOLIDCOLOR
            ColorTypeEntity.MaterialTypeGradialColor.type -> SOLIDCOLOR
            else -> NETIMAGE
        }
    }

    companion object {
        const val SOLIDCOLOR = 0
        const val NETIMAGE = 1
    }

    private class ItemClickListener(val callback: ItemClickCallback?) : View.OnClickListener {
        override fun onClick(v: View?) {
            v?.let {
                animator(it)
                val entity = it.tag as? ColorsItem
                if (entity != null) {
                    callback?.onItemClick(entity)
                }
            }
        }

        private fun animator(view: View) {
            val animator1 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.6f)
            val animator2 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.6f)
            val animatorSet1 = AnimatorSet()
            animatorSet1.duration = 200
            animatorSet1.interpolator = DecelerateInterpolator()
            animatorSet1.playTogether(animator1, animator2)

            val animator3 = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1.0f)
            val animator4 = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1.0f)
            val animatorSet2 = AnimatorSet()
            animatorSet2.duration = 200
            animatorSet2.interpolator = AccelerateInterpolator()
            animatorSet2.playTogether(animator3, animator4)

            val animatorSet3 = AnimatorSet()
            animatorSet3.playSequentially(animatorSet1, animatorSet2)
            animatorSet3.start()
        }
    }

    interface ItemClickCallback {
        fun onItemClick(colorsItem: ColorsItem)
    }
}