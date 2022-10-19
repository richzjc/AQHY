package com.micker.aqhy.util

import android.content.Context
import android.media.MediaPlayer
import com.micker.home.R

fun playErrorSuccAlarm(context: Context?, isSucc : Boolean) {
    context ?: return
    if(isSucc){
        val mMediaPlayer = MediaPlayer.create(context, R.raw.good);
        mMediaPlayer.start();
    }else{
        val mMediaPlayer = MediaPlayer.create(context, R.raw.jiayou);
        mMediaPlayer.start();
    }
}