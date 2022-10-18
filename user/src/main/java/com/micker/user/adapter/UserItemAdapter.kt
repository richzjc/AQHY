package com.micker.user.adapter

import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleAdapter
import com.micker.user.holder.UserItemViewHolder
import com.micker.user.model.UserItemEntity

class UserItemAdapter : BaseRecycleAdapter<UserItemEntity, UserItemViewHolder>(){

    override fun binderItemHolder(holder: UserItemViewHolder?, position: Int) {
        holder!!.doBindData(get(position))
    }

    override fun createListItemView(parent: ViewGroup?, viewType: Int): UserItemViewHolder {
        return UserItemViewHolder(parent?.context)
    }
}