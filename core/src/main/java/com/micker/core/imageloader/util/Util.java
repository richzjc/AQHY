package com.micker.core.imageloader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.micker.core.imageloader.ImageLoadManager;
import com.micker.core.imageloader.WscnImageView;

public class Util {


    public static Boolean getIsNoImage(Context context) {
        return getPrefs(context, "config").getBoolean("no_image", false); // 默认任何环境浏览图片
    }

    public static Boolean isConnectWIFI(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectManager.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    private static SharedPreferences getPrefs(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static boolean checkFilter(WscnImageView imageView, String url) {
        boolean result = (Util.getIsNoImage(imageView.getContext())
                && !Util.isConnectWIFI(imageView.getContext()))
                && !ImageLoadManager.checkIsInFileCache(url);
        if (result) imageView.clearImage();
        return result;
    }

    public static float dipToPx(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
