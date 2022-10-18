package com.micker.aqhy.factory.action

import androidx.appcompat.app.AlertDialog
import com.micker.aqhy.R
import com.micker.core.manager.AppManager
import com.micker.data.constant.LOGIN_ACTIVITY
import com.micker.global.user.AccountManager
import com.micker.helper.UtilsContextManager
import com.micker.helper.router.RouterHelper
import com.micker.rpc.exception.IErrorAction

/**
 * Created by wscn on 16/12/16.
 */

object ErrorTokenAction : IErrorAction {
    private var alertDialog: AlertDialog? = null

    fun getInstance() : ErrorTokenAction?{
        return this
    }

    override fun responseToErrCode() {
        AccountManager.logout()
        showAlertDialog()
    }

    private fun showAlertDialog() {
        if (alertDialog != null && alertDialog!!.isShowing)
            return

        val topActivity = AppManager.getAppManager().topActivity
        if (topActivity != null) {
            val builder = AlertDialog.Builder(topActivity, R.style.Base_Theme_AppCompat_Light_Dialog_Alert)
            builder.setCancelable(false)
            builder.setTitle("该账户信息已过期，请重新登录")
            builder.setPositiveButton(
                "去登录"
            ) { dialog, whichButton ->
                dialog.dismiss()
                RouterHelper.open(LOGIN_ACTIVITY, UtilsContextManager.getInstance().application)
            }
            builder.setNegativeButton(
                "取消"
            ) { dialog, whichButton -> dialog.dismiss() }
            alertDialog = builder.create()
            alertDialog!!.show()
        }
    }
}
