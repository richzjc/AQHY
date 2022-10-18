package com.micker.user.holder

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.micker.core.adapter.BaseRecycleViewHolder
import com.micker.core.rx.RxActivityResult
import com.micker.data.constant.LOGIN_ACTIVITY
import com.micker.global.user.AccountManager
import com.micker.helper.router.RouterHelper
import com.micker.user.R
import com.micker.user.model.UserItemEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.aqhy_recycler_item_user.view.*

class UserItemViewHolder(context : Context?) : BaseRecycleViewHolder<UserItemEntity>(context){

    init {
        itemView?.setOnClickListener {
            if (content != null) {
                if (content.isNeedLogin) {
                    if (AccountManager.isLogin()) {
                        responseToOnclick(it)
                    } else {
                        switchToLogin()
                    }
                } else {
                    responseToOnclick(it)
                }
            }
        }
        itemView?.setBackgroundColor(Color.WHITE)
    }

    private fun switchToLogin() {
        RxActivityResult(mContext as? Activity)
            .startForResult(RouterHelper.getIntentFromUrl(LOGIN_ACTIVITY, mContext))
            .filter {
                it.resultCode == Activity.RESULT_OK
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { responseToOnclick(itemView) }
            .subscribe()
    }

    private fun responseToOnclick(v : View?){
        if(content?.onClickListener != null){
            content?.onClickListener?.onClick(v)
        }else if(!TextUtils.isEmpty(content?.routerUrl))
            RouterHelper.open(content.routerUrl, mContext)
    }

    override fun getLayoutId() = R.layout.aqhy_recycler_item_user

    override fun doBindData(content: UserItemEntity?) {
        this.content = content
        itemView?.title.text = content?.title
        itemView?.right_arrow?.text = content?.rightText

        val params = itemView?.layoutParams as? ViewGroup.MarginLayoutParams
        if (params != null) {
            params?.topMargin = content?.marginTop ?: 0
            itemView?.layoutParams = params
        }
    }
}