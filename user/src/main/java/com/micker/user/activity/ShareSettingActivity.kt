package com.micker.user.activity

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.widget.IconView
import com.micker.data.constant.USER_SHARE_SETTING
import com.micker.global.const.SHARE_BOTH
import com.micker.global.const.SHARE_BOTTOM
import com.micker.global.const.SHARE_NONE
import com.micker.global.const.SHARE_TOP
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.user.R
import com.micker.user.dialog.SharePreviewDialog
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.aqhy_activity_share_setting.*

@BindRouter(urls = [USER_SHARE_SETTING])
class ShareSettingActivity : BaseActivity<Any, BasePresenter<Any>>() {

    private var selectView : View? = null

    override fun doGetContentViewId() = R.layout.aqhy_activity_share_setting

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        setTitle("保留 头部 + 尾部", both, SHARE_BOTH)
        setTitle("保留 头部", top, SHARE_TOP)
        setTitle("保留 尾部", bottom, SHARE_BOTTOM)
        setTitle("两者都不保留", none, SHARE_NONE)
    }

    private fun setTitle(s: String, view: View?, share: Int) {
        view?.setBackgroundColor(Color.WHITE)
        view?.findViewById<TextView>(R.id.title)?.text = s
        val rightView = view?.findViewById<IconView>(R.id.right_arrow)
        rightView?.setTextColor(ResourceUtils.getColor(R.color.color_1482f0))
        rightView?.text = ResourceUtils.getResStringFromId(R.string.icon_right_tick)
        rightView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        val shareValue = SharedPrefsUtil.getInt("shareSetting", SHARE_BOTH)
        if (shareValue == share) {
            view?.findViewById<IconView>(R.id.right_arrow)?.show()
            selectView = view
        }else
            view?.findViewById<IconView>(R.id.right_arrow)?.hide(true)
    }

    override fun doInitData() {
        super.doInitData()
        both?.setOnClickListener { showShareDialog(R.drawable.aqhy_share_top_bottom, SHARE_BOTH, both) }
        top?.setOnClickListener { showShareDialog(R.drawable.aqhy_share_top, SHARE_TOP, top) }
        bottom?.setOnClickListener { showShareDialog(R.drawable.aqhy_share_bottom, SHARE_BOTTOM, bottom) }
        none?.setOnClickListener { showShareDialog(R.drawable.aqhy_share_none, SHARE_NONE, none) }
    }

    private fun showShareDialog(id: Int, type: Int, view : View?) {
        val dialog = SharePreviewDialog()
        val bundle = Bundle()
        bundle.putInt("drawableId", id)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "sharePreview")
        SharedPrefsUtil.saveInt("shareSetting", type)
        if(selectView != view){
            selectView?.findViewById<IconView>(R.id.right_arrow)?.hide(true)
            view?.findViewById<IconView>(R.id.right_arrow)?.show()
            selectView = view

        }
    }
}