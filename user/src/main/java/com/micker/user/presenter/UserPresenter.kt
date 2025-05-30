package com.micker.user.presenter

import android.view.View
import com.micker.core.base.BasePresenter
import com.micker.data.constant.*
import com.micker.data.constant.USER_MAIN_SETTING_ACTIVITY
import com.micker.global.user.AccountManager
import com.micker.helper.ResourceUtils
import com.micker.helper.router.RouterHelper
import com.micker.helper.system.EquipmentUtils
import com.micker.helper.system.ScreenUtils
import com.micker.user.R
import com.micker.user.callback.UserCallback
import com.micker.user.model.UserItemEntity

class UserPresenter : BasePresenter<UserCallback>(){

    fun loadData() {
        val list = mutableListOf<UserItemEntity>()
        val rightText = ResourceUtils.getResStringFromId(R.string.icon_arrow_right)

        list.add(UserItemEntity( "我的创作", USER_MY_WORKS, true, 0, rightText))
        list.add(UserItemEntity( "官网", "https://yy0914.netlify.app/", false, 0, rightText))
        list.add(UserItemEntity("设置", USER_MAIN_SETTING_ACTIVITY, false, 0, rightText))
        if(AccountManager.isLogin()) {
            val item = UserItemEntity("退出登录", "", false, ScreenUtils.dip2px(5f), "")
            item.onClickListener = View.OnClickListener {
                AccountManager.logout()
                list.removeAt(list.size - 1)
                viewRef?.setData(list, false)
            }
            list.add(item)
        }
        viewRef?.setData(list, false)
    }

    fun loadSettingData() {
        val rightText = ResourceUtils.getResStringFromId(R.string.icon_arrow_right)
        val list = mutableListOf<UserItemEntity>()
        list.add(UserItemEntity( "分享设置", USER_SHARE_SETTING, false, ScreenUtils.dip2px(5f), rightText))

        var item = UserItemEntity("用户协议", USER_MAIN_SETTING_ACTIVITY, false, ScreenUtils.dip2px(5f), rightText)
        item.onClickListener = View.OnClickListener {
            RouterHelper.open("http://yuanyuan0914.top/protocal/aqhy_prootocol.html", it?.context)
        }
        list.add(item)

        item = UserItemEntity("隐私政策", USER_MAIN_SETTING_ACTIVITY, false, 0, rightText)
        item.onClickListener = View.OnClickListener {
            RouterHelper.open("http://yuanyuan0914.top/protocal/aqhy_privacy.html", it?.context)
        }
        list.add(item)


        item = UserItemEntity("个人信息", USER_MAIN_SETTING_ACTIVITY, false, 0, rightText)
        item.onClickListener = View.OnClickListener {
            RouterHelper.open(USER_MAIN_PERSONAL_MSG_ACTIVITY, it?.context)
        }
        list.add(item)

        item = UserItemEntity("第三方SDK目录", USER_MAIN_SETTING_ACTIVITY, false, 0, rightText)
        item.onClickListener = View.OnClickListener {
            RouterHelper.open("https://yy0914.netlify.app/aqhy_sdk.html", it?.context)
        }
        list.add(item)


        list.add(UserItemEntity("关于我们", USER_MAIN_ABOUNT_ACTIVITY, false, 0, rightText))
        list.add(UserItemEntity("版本", "", false, 0, EquipmentUtils.getVersionName()))
        if(AccountManager.isLogin()) {
            item = UserItemEntity("退出登录", "", false, ScreenUtils.dip2px(5f), "")
            item.onClickListener = View.OnClickListener {
                AccountManager.logout()
                list.removeAt(list.size - 1)
                viewRef?.setData(list, false)
            }
            list.add(item)
        }
        viewRef?.setData(list, false)
    }

}