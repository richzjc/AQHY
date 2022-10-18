package com.micker.user.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.constant.LOGIN_ACTIVITY
import com.micker.data.constant.USER_DATA_ACTIVITY
import com.micker.global.user.AccountManager.getLoginMobile
import com.micker.global.user.AccountManager.isLogin
import com.micker.global.user.AccountManager.userInfoEntity
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.ScreenUtils
import com.micker.user.R
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.tail_view_user_header.view.*

class UserHeaderView(container: ViewGroup) {
    var view: View? = null

    init {
        view = LayoutInflater.from(container.context).inflate(R.layout.tail_view_user_header, container, false)
        initView(view)
        view?.bg?.hierarchy?.setPlaceholderImage(null)
    }


    private fun initView(v: View?) {
        update()
        v?.setOnClickListener {
            if (!isLogin()) {
                RouterHelper.open(LOGIN_ACTIVITY, v.context)
            } else {
                RouterHelper.open(USER_DATA_ACTIVITY, v.context)
            }
        }
    }

    fun update() {
        if (isLogin()) {
            view?.user_name?.text = "${userInfoEntity?.baseUser?.nickName}"
            ImageLoadManager.loadImage("${userInfoEntity?.baseUser?.avatar}", view?.avatar, 0)
            view?.mobile?.show()
            view?.mobile?.text = getLoginMobile()
            val params = view?.user_name?.layoutParams as? RelativeLayout.LayoutParams
            params?.bottomMargin = ScreenUtils.dip2px(10f)
            view?.user_name?.layoutParams = params
        } else {
            view?.user_name?.text = "登录/注册"
            ImageLoadManager.loadImage(R.drawable.default_img, view?.avatar, 0)
            view?.mobile?.hide(true)
            val params = view?.user_name?.layoutParams as? RelativeLayout.LayoutParams
            params?.bottomMargin = ScreenUtils.dip2px(20f)
            view?.user_name?.layoutParams = params
        }
        loadBlur()
    }

    private fun loadBlur() {
        view?.bg?.show()
        if (isLogin()) {
            val params = view?.bg?.layoutParams as RelativeLayout.LayoutParams
            params.width = ScreenUtils.getScreenWidth()
            params.height = ScreenUtils.dip2px(200f)
            view?.bg?.layoutParams = params
            ImageLoadManager.loadBlurImage("${userInfoEntity?.baseUser?.avatar}", view?.bg, 10, 20)
        } else {
            val params = view?.bg?.layoutParams as RelativeLayout.LayoutParams
            params.width = ScreenUtils.getScreenWidth()
            params.height = ScreenUtils.dip2px(170f)
            view?.bg?.layoutParams = params
            ImageLoadManager.loadBlurImage(R.drawable.default_img, view?.bg, 10, 20)
        }
    }
}