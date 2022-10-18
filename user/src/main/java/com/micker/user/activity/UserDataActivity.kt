package com.micker.user.activity

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.data.constant.USER_DATA_ACTIVITY
import com.micker.global.user.AccountManager
import com.micker.global.user.AccountManager.userInfoEntity
import com.micker.user.R
import kotlinx.android.synthetic.main.aqhy_activity_user_data.*

@BindRouter(urls = [USER_DATA_ACTIVITY])
class UserDataActivity : BaseActivity<Any, BasePresenter<Any>>() {

    override fun doGetContentViewId() = R.layout.aqhy_activity_user_data

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        ImageLoadManager.loadImage(userInfoEntity?.baseUser?.avatar ?: "", headImg, 0)
        nickName?.setRightText(userInfoEntity?.baseUser?.nickName ?: "")

        bindTel?.setRightText(userInfoEntity?.baseUser?.phone ?: "")

        logout?.setOnClickListener {
            AccountManager.logout()
            finish()
        }
    }
}