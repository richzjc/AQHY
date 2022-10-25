package com.wallstreetcn.global.media.widget;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.util.HashMap;

public class FirstFrameUtils {
    public static Bitmap getFirstFrame(String url) {
        return getFrame(url, 1000);
    }

    public static Bitmap getFrame(String url, long timeUs) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(url, new HashMap<>());
        return mediaMetadataRetriever.getFrameAtTime(timeUs); //unit in microsecond
    }
}
