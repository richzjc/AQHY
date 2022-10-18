package com.micker.core.activity

import android.Manifest
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.micker.core.R
import com.micker.core.activity.GestureSetup
import com.micker.core.adapter.image.PaintingsPagerAdapter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.core.imageloader.ImageLoadManager
import com.micker.helper.file.FileUtil
import com.micker.helper.text.TextUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.tail_activity_image_gallery.*

class ImageGalleryActivity : BaseActivity<Any, BasePresenter<Any>>(), ViewPager.OnPageChangeListener {

    var images: List<String>? = null

    override fun doGetContentViewId() = R.layout.tail_activity_image_gallery

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        viewPager?.addOnPageChangeListener(this)
        val bundle = intent.extras
        images = bundle?.getStringArrayList("images")
        val index = bundle?.getInt("index", 0) ?: 0
        onLoadFinish(index, images)
        setListener()
    }

    private fun onLoadFinish(index: Int, images: List<String>?) {
        images?.let {
            val adapter = PaintingsPagerAdapter(viewPager, images, GestureSetup())
            viewPager.adapter = adapter
            viewPager.currentItem = index
            val indicatorStr = TextUtil.format(
                "%d / %d", index + 1,
                images!!.size
            )
            indicator.text = indicatorStr
            viewPager.addOnPageChangeListener(this)
        }
    }

    private fun setListener() {
        save?.setOnClickListener {
            if (images != null && images!!.isNotEmpty()) {
                val index = viewPager?.currentItem ?: 0
                val imageUrl = images!![index]
                ImageLoadManager.loadBitmap(imageUrl) { bitmap ->
                    val rxPermissions = RxPermissions(this@ImageGalleryActivity)
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .doOnNext {
                            FileUtil.saveBitmapToCamera(this@ImageGalleryActivity, bitmap)
                        }
                        .doOnError {
                            it.printStackTrace()
                        }
                        .subscribe()
                }
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        val indicatorStr = TextUtil.format(
            "%d / %d", position + 1,
            images!!.size
        )
        indicator.text = indicatorStr
    }

}