package com.micker.user.activity

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.data.constant.USER_FORGET_PASSWORD
import com.micker.data.constant.USER_REGIST_ACCOUNT
import com.micker.global.manager.MsgCountDownTimer
import com.micker.helper.snack.MToastHelper
import com.micker.helper.text.PhoneFormatWatcher
import com.micker.user.R
import com.micker.user.callback.ForgetPasswordCallback
import com.micker.user.presenter.ForgetPasswordPresenter
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_activity_forget_regist.*
import kotlinx.android.synthetic.main.aqhy_view_forget_regist.*

@BindRouter(urls = [USER_FORGET_PASSWORD, USER_REGIST_ACCOUNT])
class ForgetRegistActivity : BaseActivity<ForgetPasswordCallback, ForgetPasswordPresenter>(), ForgetPasswordCallback,
    TextWatcher {
    private var countDownTimer: CountDownTimer? = null
    private val isRegsit by lazy { intent?.extras?.getBoolean("isRegist", true) ?: true }
    override fun doGetContentViewId() = R.layout.aqhy_activity_forget_regist

    override fun doGetPresenter() = ForgetPasswordPresenter()

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        initTitleBar()
        setListener()
        userNameEdit?.addTextChangedListener(this)
        userNameEdit?.addTextChangedListener(PhoneFormatWatcher(userNameEdit))
        passwordEdit?.addTextChangedListener(this)
        nickName?.addTextChangedListener(this)
        code?.addTextChangedListener(this)
    }

    private fun initTitleBar() {
        if (isRegsit) {
            titlebar?.title = "注册"
            nickName?.show()
            nickName_line?.show()
            main_btn_login?.text = "注册"
        } else {
            titlebar?.title = "忘记密码"
            nickName?.hide(true)
            nickName_line?.hide(true)
            main_btn_login?.text = "提交"
        }
    }

    private fun setListener() {
        send_code?.setOnClickListener {
            var username = userNameEdit.text.toString()
            if (TextUtils.isEmpty(username)) {
                MToastHelper.showToast("请输入手机号")
            } else {
                username = if (TextUtils.isEmpty(username)) "" else username.replace(" ", "")
                mPresenter?.getVerifyCode(username)
            }
        }

        main_btn_login?.setOnClickListener {
            var phone = userNameEdit.text.toString()
            if (TextUtils.isEmpty(phone)) {
                MToastHelper.showToast("请输入手机号")
                return@setOnClickListener
            }

            var password = passwordEdit.text.toString()
            if (TextUtils.isEmpty(password)) {
                MToastHelper.showToast("请输入密码")
                return@setOnClickListener
            }

            if (password.length < 6) {
                MToastHelper.showToast("密码长度不能少于6位")
                return@setOnClickListener
            }

            if (isRegsit) {
                var nickname = nickName.text.toString()
                if (TextUtils.isEmpty(nickname)) {
                    MToastHelper.showToast("请输入昵称")
                    return@setOnClickListener
                }
            }

            var code = code.text.toString()
            if (TextUtils.isEmpty(code)) {
                MToastHelper.showToast("请输入验证码")
                return@setOnClickListener
            }
            main_btn_login?.startAnim()
            if (isRegsit) {
                mPresenter?.regist(phone.replace(" ", ""), code, password, nickName.text.toString())
            } else {
                mPresenter?.changePwd(phone.replace(" ", ""), code, password)
            }
        }
    }

    override fun onMsgSendSuccess() {
        send_code?.isEnabled = false
        send_code.setTextColor(ContextCompat.getColor(this, R.color.color_999999))
        countDownTimer = MsgCountDownTimer(send_code, 60 * 1000)
        countDownTimer?.start()
    }

    override fun changePwdSuccess() {
        main_btn_login?.gotoNew()
        finish()
    }

    override fun registSuccess() {
        main_btn_login?.gotoNew()
        finish()
    }

    override fun loginChangeError() {
        main_btn_login?.regainBackground()
        if (isRegsit)
            main_btn_login?.text = "注册"
        else
            main_btn_login?.text = "提交"
    }

    override fun afterTextChanged(s: Editable?) {
        var userName = userNameEdit?.text?.toString()
        userName = if (TextUtils.isEmpty(userName)) "" else userName?.replace(" ", "")
        val password = passwordEdit?.text?.toString()?.replace(" ", "")
        val nickname = nickName?.text?.toString()?.replace(" ", "")
        val code = code?.text?.toString()?.replace(" ", "")
        val isEnable = !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(code)
        if (isRegsit) {
            main_btn_login.isEnabled = (isEnable && !TextUtils.isEmpty(nickname))
        } else {
            main_btn_login.isEnabled = isEnable
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}