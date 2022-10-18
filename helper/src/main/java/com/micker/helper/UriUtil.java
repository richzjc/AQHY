package com.micker.helper;

import android.net.Uri;
import android.text.TextUtils;

public class UriUtil {

    public static String addParamsToUrl(String originUrl, String key, String value){
        if(TextUtils.isEmpty(originUrl) || TextUtils.isEmpty(key))
            return originUrl;
        else{
            Uri uri = Uri.parse(originUrl);
            String url = uri.buildUpon().appendQueryParameter(key, value).build().toString();
            return url;
        }
    }
}
