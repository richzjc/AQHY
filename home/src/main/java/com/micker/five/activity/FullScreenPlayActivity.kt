package com.micker.five.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.global.VIDEO_FULL_SCREEN_PLAY_ACTION
import com.micker.home.R
import com.wallstreetcn.global.media.WscnMediaEntity
import com.wallstreetcn.global.media.controller.Config
import kotlinx.android.synthetic.main.live_room_activity_full_player.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.ArrayList

@BindRouter(urls = [VIDEO_FULL_SCREEN_PLAY_ACTION])
class FullScreenPlayActivity : BaseActivity<Any, BasePresenter<Any>>() {



    private var mediaController: VideoMediaController? = null

    override fun doGetContentViewId(): Int {
        return R.layout.live_room_activity_full_player
    }

    override fun doInitSubViews(view: View) {
        cursetStatusBarTranslucentCompat()
        super.doInitSubViews(view!!)
        mediaController = VideoMediaController(this)
        val config = Config.Builder()
            .speedEnable(true)
            .build()
        config.isFullScreen = true
        videoView!!.setMediaController(mediaController)
        videoView!!.setOnPreparedListener { iMediaPlayer: IMediaPlayer? -> videoView!!.start() }
    }

    private fun cursetStatusBarTranslucentCompat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            var visibility = window.decorView.systemUiVisibility
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            visibility = visibility or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            visibility = visibility or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = visibility
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }


    override fun doInitData() {
        super.doInitData()

        var url = intent.getStringExtra("url")
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }

        val entity = WscnMediaEntity.buildEntityWithKey(url, url)
        videoView!!.mediaEntity = entity
        videoView!!.postDelayed({ videoView!!.start() }, 1000)
    }

    override fun finish() {
        videoView!!.stopPlayback()
        super.finish()
    }

    override fun onDestroy() {
        setResult(RESULT_OK)
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        videoView!!.pause()
    }
}