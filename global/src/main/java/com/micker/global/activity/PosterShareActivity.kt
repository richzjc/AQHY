package com.micker.global.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.data.constant.POSTER_SHARE_ACTION
import com.micker.global.R
import com.micker.global.callback.ISharePoster
import com.micker.global.callback.PermissionCallback
import com.micker.global.util.PermissionUtils
import com.micker.helper.ResourceUtils
import com.micker.helper.file.QDUtil
import com.micker.helper.file.QDUtil.getShareImageCache
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.aqhy_activity_share_poster.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File


@BindRouter(urls = [POSTER_SHARE_ACTION])
class PosterShareActivity : BaseActivity<Any, BasePresenter<Any>>() {

    private var poster: ISharePoster? = null

    override fun doGetContentViewId() = R.layout.aqhy_activity_share_poster

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        val holder = intent?.getStringExtra("holder")
        if(holder == null){
            CrashReport.postCatchedException(Throwable("holder 取出的值为空"))
        }else{
            val parcel = intent?.getParcelableExtra<Parcelable>("parcel")
            poster = Class.forName(holder).newInstance() as? ISharePoster
            poster?.initView(sv_content)
            poster?.bindData(parcel)
        }
    }

    override fun doInitData() {
        super.doInitData()
        titleBar?.setRightBtn2OnclickListener {
            PermissionUtils.requestPermission(
                this@PosterShareActivity,
                object : PermissionCallback {
                    override fun requestPermissSuccess() {
                        val path = QDUtil.realSave(
                            this@PosterShareActivity,
                            convertViewToBitmap(sv_content.getChildAt(0))
                        )
                        if (TextUtils.isEmpty(path))
                            return

                        Luban.with(this@PosterShareActivity)
                            .load(path)
                            .setTargetDir(
                                File(
                                    getShareImageCache(this@PosterShareActivity).absolutePath,
                                    "${System.currentTimeMillis()}"
                                ).absolutePath
                            )
                            .ignoreBy(300)
                            .setCompressListener(object : OnCompressListener {
                                override fun onSuccess(file: File?) {
                                    startShare(file?.absolutePath ?: path)
                                }

                                override fun onError(e: Throwable?) {
                                    startShare(path)
                                }

                                override fun onStart() {
                                }

                            })
                            .launch()
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun startShare(path: String?) {
        val imgUri = QDUtil.getImageContentUri(this@PosterShareActivity, path)
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        var uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = imgUri
        } else {
            uri = Uri.parse(path)

        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        shareIntent =
            Intent.createChooser(shareIntent, ResourceUtils.getResStringFromId(R.string.app_name))
        startActivity(shareIntent)
    }

    private fun convertViewToBitmap(v: View): Bitmap? {
        return try {
            val screenshot = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            v.draw(Canvas(screenshot))
            screenshot
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}