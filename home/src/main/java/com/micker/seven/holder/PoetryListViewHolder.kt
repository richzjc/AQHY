package com.micker.seven.holder

import com.micker.core.adapter.BaseRecycleViewHolder
import android.content.Context
import android.content.Intent
import com.micker.helper.text.SpannedHelper
import com.micker.home.R
import com.micker.seven.activity.SevenActivity
import com.micker.seven.model.SevenModelEnitity
import kotlinx.android.synthetic.main.aqhy_recycler_item_poetry_list.view.*

class PoetryListViewHolder(context: Context?) : BaseRecycleViewHolder<SevenModelEnitity>(context) {
    init {
        itemView?.setOnClickListener {
            val intent = Intent(mContext, SevenActivity::class.java)
            intent.putExtra("position", bindPosition)
            mContext.startActivity(intent)
        }
    }

    var bindPosition: Int = 0

    override fun getLayoutId() = R.layout.aqhy_recycler_item_poetry_list

    override fun doBindData(content: SevenModelEnitity?) {
        super.content = content
        itemView?.title?.text = content?.title
        itemView?.author?.text = content?.author
        itemView?.content?.text = SpannedHelper.trim(content?.content)
    }
}