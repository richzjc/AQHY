package com.micker.core.imageloader.util;

import android.net.Uri;

import java.io.File;

/**
 * Created by Leif Zhang on 2017/5/3.
 * Email leifzhanggithub@gmail.com
 */

public class UriHelper {
    public static Uri createUri(String url) {
        Uri uri;
        if (url.contains("http") || url.contains("https")) {
            uri = Uri.parse(url);
        } else {
            uri = Uri.fromFile(new File(url));
        }
        return uri;
    }
}
