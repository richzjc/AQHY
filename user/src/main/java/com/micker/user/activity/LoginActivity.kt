package com.micker.user.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.data.constant.ACCOUNT_STATE_CHANGED
import com.micker.data.constant.LOGIN_ACTIVITY
import com.micker.data.constant.USER_FORGET_PASSWORD
import com.micker.data.constant.USER_REGIST_ACCOUNT
import com.micker.data.model.user.AccountUserInfoEntity
import com.micker.global.user.AccountManager
import com.micker.global.user.AccountManager.getLoginMobile
import com.micker.helper.observer.ObserverManger
import com.micker.helper.router.RouterHelper
import com.micker.helper.snack.MToastHelper
import com.micker.helper.text.PhoneFormatWatcher
import com.micker.user.R
import com.micker.user.callback.LoginCallback
import com.micker.user.presenter.LoginPresenter
import kotlinx.android.synthetic.main.aqhy_activity_login.*
import kotlinx.android.synthetic.main.aqhy_view_input_layout.*

@BindRouter(urls = [LOGIN_ACTIVITY])
class LoginActivity : BaseActivity<LoginCallback, LoginPresenter>(), LoginCallback, TextWatcher {

    override fun doGetContentViewId() = R.layout.aqhy_activity_login
    override fun doGetPresenter() = LoginPresenter()
    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        userNameEdit?.setText(getLoginMobile())
        userNameEdit?.addTextChangedListener(this)
        userNameEdit?.addTextChangedListener(PhoneFormatWatcher(userNameEdit))
        passwordEdit?.addTextChangedListener(this)
        initListener()
    }

    private fun initListener() {
        titlebar?.setRightBtn2OnclickListener {
            val bundle = Bundle()
            bundle.putBoolean("isRegist", true)
            RouterHelper.open(USER_REGIST_ACCOUNT, this, bundle)
        }
        main_btn_login?.setOnClickListener {
            var username = userNameEdit?.text?.toString()
            var password = passwordEdit?.text?.toString()
            if (TextUtils.isEmpty(username)) {
                MToastHelper.showToast("请输入手机号")
            } else {
                username = if (TextUtils.isEmpty(username)) "" else username?.replace(" ", "")
                main_btn_login?.startAnim()
                mPresenter?.login(username, password)
            }
        }
        forget_password?.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isRegist", false)
            RouterHelper.open(USER_FORGET_PASSWORD, this, bundle)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        var userName = userNameEdit?.text?.toString()
        userName = if (TextUtils.isEmpty(userName)) "" else userName?.replace(" ", "")
        val password = passwordEdit?.text?.toString()?.replace(" ", "")
        val isEnable = !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)
        main_btn_login.isEnabled = isEnable
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onLoginSuccess(entity: AccountUserInfoEntity?) {
        AccountManager.saveUserInfo(entity)
        ObserverManger.getInstance().notifyObserver(ACCOUNT_STATE_CHANGED, entity)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("entity", entity)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        main_btn_login?.gotoNew()
        finish()
    }

    override fun onLoginError() {
        main_btn_login?.regainBackground()
    }
}