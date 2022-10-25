package com.wallstreetcn.global.media

import android.content.Context
import android.view.WindowManager
import com.google.android.exoplayer2.ExoPlaybackException
import com.micker.core.manager.AppManager
import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer

class WscnIjkExoMediaPlayer(context: Context) : IjkExoMediaPlayer(context) {

    var isSilent: Boolean = false
    private var isPrepare: Boolean = false
    private var playTime = 0L
    private var position = 0L
        set(value) {
            if (value > 0)
                field = value
        }

    private var totalTime = 0L
        set(value) {
            if (value > 0)
                field = value
        }

    private var startTime: Long = 0
    fun getPlayTime(): Long {
        return playTime
    }

    fun getPlayPosition(): Float {
        if (totalTime > 0)
            return (position * 100.0f) / totalTime;
        else
            return 0f;
    }

    fun getTotalTime(): Long {
        return totalTime
    }

    override fun start() {
        super.start()
        requestScreenOn()
        startTime = System.currentTimeMillis()
        totalTime = duration
    }

    override fun stop() {
        super.stop()
        removeScreenOn()
        if (startTime > 0) {
            playTime += (System.currentTimeMillis() - startTime)
            startTime = 0
        }
        totalTime = duration
        position = currentPosition
    }

    override fun pause() {
        totalTime = duration
        position = currentPosition
        super.pause()
        removeScreenOn()
        if (startTime > 0) {
            playTime += (System.currentTimeMillis() - startTime)
            startTime = 0
        }
    }

    override fun reset() {
        totalTime = duration
        position = currentPosition
        super.reset()
        removeScreenOn()
        if (startTime > 0) {
            playTime += (System.currentTimeMillis() - startTime)
            startTime = 0
        }
        totalTime = duration
        position = currentPosition
    }

    override fun release() {
        super.release()
        removeScreenOn()
        if (startTime > 0) {
            playTime += (System.currentTimeMillis() - startTime)
            startTime = 0
        }
    }

    private fun requestScreenOn() {
        val activity = AppManager.getAppManager().topActivity
        activity?.also {
            if (it.isDestroyed || it.isFinishing)
                return
            it.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun removeScreenOn() {
        val activity = AppManager.getAppManager().topActivity
        activity?.also {
            if (it.isDestroyed || it.isFinishing)
                return

            it.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun prepareAsync() {
        if (!isPrepare) {
            super.prepareAsync()
            isPrepare = true
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        super.onPlayerError(error)
        error?.cause?.message
    }


    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        if (isSilent)
            super.setVolume(0f, 0f)
        else
            super.setVolume(leftVolume, rightVolume)
    }
}