package com.micker.aqhy.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import com.micker.aqhy.R
import com.micker.core.base.BaseDialogFragment
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.router.RouterHelper
import com.micker.helper.snack.MToastHelper
import com.micker.helper.system.ScreenUtils
import kotlinx.android.synthetic.main.aqhy_dialog_user_privacy.*

class UserPrivacyDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            return@setOnKeyListener keyCode == KeyEvent.KEYCODE_BACK
        }
        return dialog
    }

    override fun doGetContentViewId()  = R.layout.aqhy_dialog_user_privacy

    override fun getDialogWidth(): Int {
        return ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(70f)
    }

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }

    override fun doInitData() {
        setListener()
        content?.movementMethod = LinkMovementMethod()
        val builder = SpannableStringBuilder()
        builder.append(ResourceUtils.getResStringFromId(R.string.alien_user_privacy_content))
            .append("\n\n")
            .append(ResourceUtils.getResStringFromId(R.string.alien_user_privacy_hint))
            .append("<<")
        val start = builder.length
        builder.append(ResourceUtils.getResStringFromId(R.string.alien_user_privacy_text))
        val end = builder.length
        builder.append(">>和")

        builder.append("<<")
        val start1 = builder.length
        builder.append(ResourceUtils.getResStringFromId(R.string.alien_private_protocol))
        val end1 = builder.length
        builder.append(">>")
        builder.append("。")
        builder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouterHelper.open("https://314.la/terms/aqhy_protocol.html", context)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ResourceUtils.getColor(R.color.text_link)
            }
        }, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        builder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouterHelper.open("https://314.la/terms/aqhy_privacy.html", context)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ResourceUtils.getColor(R.color.text_link)
            }
        }, start1, end1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        content?.text = builder
    }

    private fun setListener() {
        user_agree?.setOnClickListener {
            SharedPrefsUtil.save("userPrivacy", false)
            dismiss()
        }

        not_agree?.setOnClickListener {
            MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.alien_user_privacy_not_agree_hint))
        }
    }
}