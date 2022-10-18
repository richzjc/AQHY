package com.micker.core.imageloader;


import com.facebook.imageutils.BitmapUtil;

public class ByteConstants {
    public static final int KB = 1024;
    public static final int MB = 1024 * KB;
    public static final int IMAGE_MAX_SIZE = 2048;
    public static final int MAX_BITMAP_SIZE = Math.round(BitmapUtil.MAX_BITMAP_SIZE * 10);

    private ByteConstants() {
    }
}