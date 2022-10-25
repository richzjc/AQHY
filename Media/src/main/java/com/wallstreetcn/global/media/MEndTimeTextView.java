package com.wallstreetcn.global.media;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wallstreetcn.baseui.widget.CustomTextView;

import java.util.Formatter;
import java.util.Locale;

public class MEndTimeTextView  extends CustomTextView {

    private MediaController.MediaPlayerControl player;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;

    public MEndTimeTextView(Context context) {
        super(context);
        init();
    }

    public MEndTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MEndTimeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMediaPlayer(MediaController.MediaPlayerControl player){
        this.player = player;
    }

    private void init(){
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        CharSequence showText = text;
        if(player != null){
            showText = stringForTime(player.getDuration()  - player.getCurrentPosition());
        }
        super.setText(showText, type);
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
