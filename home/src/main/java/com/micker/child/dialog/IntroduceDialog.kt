package com.micker.child.dialog

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.micker.core.base.BaseDialogFragment
import com.micker.global.util.ShapeDrawable
import com.micker.helper.ResourceUtils
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_dialog_introduce.*
import com.micker.home.activity.MainActivity
import java.lang.Exception


class IntroduceDialog : BaseDialogFragment() {

    private val descValue = "孩子的教育，需要从小就开始培养。尤其是在大城市里面，孩子的竞争压力还是相当大的。\n\n" +
            "安琪花园，着重培养孩子的观察能力，记忆能力，逻辑推理能力，以及其它方面的综合知识。 \n\n" +
            "此APP是我会自己小孩写的一款APP, 现上架到应用市场分享给大家。 大家有好的建议，或者有什么利于孩子的功能都可以在应用市场反馈给我。 我会开发完了重新上传到应用市场。\n\n" +
            "感谢！！！  感谢！！！"

    private val drawable by lazy {
        val color = ResourceUtils.getColor(R.color.color_1482f0)
        ShapeDrawable.getDrawable(0, ScreenUtils.dip2px(5f), color, color)
    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }


    override fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun doGetContentViewId() = R.layout.aqhy_dialog_introduce

    override fun doInitData() {
        super.doInitData()
        desc?.text = descValue
        confirm?.background = drawable
        confirm?.setOnClickListener {
            try {
                val uri: Uri = Uri.parse("market://details?id=" + context?.packageName)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dismiss()
            } catch (e: Exception) {
                Toast.makeText(context, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                dismiss()
            }
        }
    }
}