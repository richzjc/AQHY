package com.micker.four.activity

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.listener.FinishSelectListener
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.first.callback.NanduCallback
import com.micker.first.callback.SuccCallback
import com.micker.first.dialog.NanduDialog
import com.micker.global.FOUR_STAGE_ROUTER
import com.micker.global.THIRD_STAGE_ROUTER
import com.micker.global.const.imagesArry
import com.micker.global.const.pingTuArr
import com.micker.global.dialog.selectPicFromCamera
import com.micker.global.dialog.selectPicFromCameraOrPic
import com.micker.helper.ResourceUtils
import com.micker.helper.SharedPrefsUtil
import com.micker.helper.system.ScreenUtils
import com.micker.home.R
import com.tbruyelle.rxpermissions3.RxPermissionsNew
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.*
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.bg
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.edit_stage
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.nandu
import kotlinx.android.synthetic.main.aqhy_activity_four_stage.reset
import java.util.ArrayList
import kotlin.random.Random

@BindRouter(urls = [FOUR_STAGE_ROUTER])
class FourStageActivity : BaseActivity<Any, BasePresenter<Any>>() {


    private val nanDuCallback by lazy {
        object : NanduCallback {
            override fun nanduCallback(realjieshu: Int, isBiHua: Boolean) {
                if (!isBiHua) {
                    var jieshu = realjieshu
                    if (jieshu < 3)
                        jieshu = 3
                    else if (jieshu > 6)
                        jieshu = 6
                    SharedPrefsUtil.saveIntForInstance("four_stage_jieshu", jieshu)
                    reset?.performClick()
                }
            }
        }
    }


    private val succCallback by lazy {
        object : SuccCallback {
            override fun succCallback() {
                setData(this)
            }
        }
    }

    private fun setData(succCallback: SuccCallback) {
        val jieshu = SharedPrefsUtil.getInt("four_stage_jieshu", 3)
        val size = pingTuArr.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadBitmap(pingTuArr[index]) {
            if (it != null) {
                edit_stage.bindData(card_view, jieshu, it, succCallback)
                setNotEditBitmap(it)
            } else {
                val drawable = ResourceUtils.getResDrawableFromID(R.drawable.defalut_pingtu_img)
                val bitmapDrawable = drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                edit_stage?.bindData(card_view, jieshu, bitmap, succCallback)
                setNotEditBitmap(bitmap)
            }
        }
    }

    private fun setNotEditBitmap(bitmap: Bitmap) {
        not_edit_stage?.also {

            var widthSize1 = ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(30f)
            val heightSize = ScreenUtils.getScreenHeight() / 3 - ScreenUtils.dip2px(50f)
            val tempHeight = (bitmap!!.height * 1f / bitmap!!.width) * widthSize1

            var realHeightSize: Int
            var widthSize: Int


            if (tempHeight > heightSize) {
                realHeightSize = heightSize
                widthSize = ((bitmap!!.width * 1f / bitmap!!.height) * realHeightSize).toInt()
            }else{
                widthSize = widthSize1
                realHeightSize = ((bitmap!!.height * 1f / bitmap!!.width) * widthSize1).toInt()
            }

            val params = FrameLayout.LayoutParams(widthSize, realHeightSize)
            it.layoutParams = params
            it.scaleType = ImageView.ScaleType.FIT_XY
            it.setImageBitmap(bitmap)
        }
    }

    override fun doGetContentViewId() = R.layout.aqhy_activity_four_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        initBg()
    }

    private fun initBg() {
        val size = imagesArry.size
        val index = Random.nextInt(size)
        ImageLoadManager.loadImage(imagesArry[index], bg, 0, false)
    }


    override fun doInitData() {
        super.doInitData()
        setListener()
        setData(succCallback)
    }


    private fun setListener() {
        nandu.setOnClickListener {
            val dialog = NanduDialog()
            val bundle = Bundle()
            bundle.putBoolean("isBiHua", false)
            bundle.putString("hint", "输入3~6的数字,越大越难")
            bundle.putString("title", "难度系数")
            dialog.arguments = bundle
            dialog?.nanduCallback = nanDuCallback
            dialog.show(supportFragmentManager, "thirdNanDu")
        }

        reset?.setOnClickListener {
            val jieshu = SharedPrefsUtil.getInt("four_stage_jieshu", 3)
            edit_stage?.bindData(card_view, jieshu, edit_stage.originBitmap, succCallback)
        }

        select?.setOnClickListener {

            selectPicFromCameraOrPic(this,
                1,
                null,
                FinishSelectListener { resultCode, data ->
                    if (resultCode == RESULT_OK) {
                        val resultPhotos: ArrayList<Photo>? =
                            data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)
                        if (resultPhotos != null && resultPhotos.size > 0) {
                            val photo = resultPhotos[0]
                            val bitmap = BitmapFactory.decodeFile(photo.path)
                            val jieshu = SharedPrefsUtil.getInt("four_stage_jieshu", 3)
                            edit_stage?.bindData(card_view, jieshu, bitmap, succCallback)
                            setNotEditBitmap(bitmap)
                        }
                    }
                })
        }
    }
}