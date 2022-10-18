package com.micker.home.activity

import android.os.Bundle
import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.data.constant.SVG_DETAIL_ACTIVITY
import com.micker.data.model.home.ColorCategoryEntity
import com.micker.data.model.home.ColorGroupDetailEntity
import com.micker.data.model.home.HomeSubItemEntity
import com.micker.data.model.home.StatusActionEntity
import com.micker.helper.snack.MToastHelper
import com.micker.home.R
import com.micker.home.callback.SvgDetailCallback
import com.micker.home.dialog.DraftPublishDialog
import com.micker.home.presenter.SvgDetailPresenter
import com.micker.home.singleton.ColorHistoryFactory
import com.micker.home.singleton.ColorsItemFactory
import kotlinx.android.synthetic.main.aqhy_activity_svg_detail.*
import kotlinx.android.synthetic.main.aqhy_view_svg_detail_content.view.*

@BindRouter(urls = [SVG_DETAIL_ACTIVITY])
class SvgDetailActivity : BaseActivity<SvgDetailCallback, SvgDetailPresenter>(), SvgDetailCallback {

    val entity by lazy {
        intent.extras!!.getParcelable<HomeSubItemEntity>("entity")
    }

    override fun doGetPresenter(): SvgDetailPresenter {
        return SvgDetailPresenter(entity?.svgUrl)
    }

    override fun loadSvgSucc(svgPath: String?, callbackId: String?) {
        content_view?.loadSvgSucc(svgPath, callbackId, entity!!.reset)
    }

    override fun loadSvgError() {
        dismissDialog()
        content_view?.loadSvgError()
    }

    override fun loadColorCategory(list: ArrayList<ColorCategoryEntity>) {
        footer_view?.loadColorCategorySuccess(list)
        mPresenter?.loadColorListByCategory(list[0].type!!.type)
    }

    override fun doGetContentViewId() = R.layout.aqhy_activity_svg_detail

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        showDialog()
        header_view.wscnWebView = content_view.wscnWebView
        ColorsItemFactory.init()
        content_view?.mPresenter = mPresenter
        content_view?.bindHomeSubItemEntity(entity)
    }

    override fun loadColorList(entity: ColorGroupDetailEntity?) {
        footer_view?.loadColorListSuccess(entity)
    }

    override fun onDestroy() {
        super.onDestroy()
        ColorHistoryFactory.clear()
        ColorsItemFactory.clear()
    }

    override fun statusAction(entity: StatusActionEntity?) {
        header_view?.bindStatusData(entity)
    }

    override fun isNeedSwipeBack(): Boolean {
        return false
    }

    override fun uploadError() {
        MToastHelper.showToast("上传失败")
        header_view?.hideLoading()
    }

    override fun uploadSuccess(bundle: Bundle) {
        header_view?.hideLoading()
        val dialog = DraftPublishDialog()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "draftDialog")
    }
}