package com.micker.home.dialog

import android.app.Dialog
import android.view.Gravity
import com.micker.core.base.BaseDialogFragment
import com.micker.data.constant.USER_MY_WORKS
import com.micker.helper.router.RouterHelper
import com.micker.helper.snack.MToastHelper
import com.micker.home.R
import com.micker.home.api.SvgUserUpdateApi
import com.micker.rpc.ResponseListener
import kotlinx.android.synthetic.main.aqhy_dialog_draft_publish.*

class DraftPublishDialog : BaseDialogFragment() {

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun getDialog(): Dialog? {
        val dialog = super.getDialog()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        return dialog
    }

    override fun doGetContentViewId(): Int {
        return R.layout.aqhy_dialog_draft_publish
    }

    override fun doInitData() {
        super.doInitData()
        val window = dialog?.window
        val lp = window?.attributes
        lp?.windowAnimations = R.style.anim_menu_bottombar
        window?.attributes = lp

        draft?.setOnClickListener {
            updateContent(0)
        }

        publish?.setOnClickListener {
            updateContent(1)
        }
    }

    private fun updateContent(status : Int) {
        SvgUserUpdateApi(object : ResponseListener<String> {
            override fun onSuccess(model: String?, isCache: Boolean) {
                dismiss()
                RouterHelper.open(USER_MY_WORKS, context)
                activity?.finish()
            }

            override fun onErrorResponse(code: Int, error: String?) {
                MToastHelper.showToast("操作失败")
                dismiss()
            }

        }, arguments, status).start()
    }
}