package com.micker.helper;

import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.ContextCompat;

import com.micker.helper.snack.MToastHelper;
import com.micker.helper.system.ScreenUtils;

/**
 * Created by zhangjianchuan on 2016/6/23.
 */
public class Util {

    public static int getFontIndex() {
        return SharedPrefsUtil.getInt("font", 15); // 默认中字体
    }

    public static void setFontIndex(int fontIndex) {
        SharedPrefsUtil.saveInt("font", fontIndex);
    }

    public static int getFontIndexValue() {
        return SharedPrefsUtil.getInt("fontIndex", 1);
    }

    public static void setFontIndexValue(int fontIndexValue) {
        SharedPrefsUtil.saveInt("fontIndex", fontIndexValue);
    }

    public static String weekdayConvert(Calendar calendar) {
        String weekDay = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_one_text);
                break;
            case 3:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_two_text);
                break;
            case 4:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_three_text);
                break;
            case 5:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_four_text);
                break;
            case 6:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_five_text);
                break;
            case 7:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_six_text);
                break;
            case 1:
                weekDay = ResourceUtils.getResStringFromId(R.string.helper_day_text);
                break;
            default:
                break;
        }
        return ResourceUtils.getResStringFromId(R.string.helper_week_text) + weekDay;
    }


    public static Boolean getIsNoImage() {
        return SharedPrefsUtil.getBoolean("config", "no_image", false); // 默认任何环境浏览图片
    }


    public static Boolean isConnectWIFI() {
        try {
            ConnectivityManager connectManager = (ConnectivityManager) getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectManager.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPayment() {
        String metaDate = ResourceUtils.getMetaDateFromName("WALL_PAYMENT");
        return TextUtils.equals("PAYMENT", metaDate);
    }

    public static boolean isGooglePlayChannel() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_GOOGLE", metaData);
    }

    public static boolean isHWOversea() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_HUAWEI_OVERSEA", metaData);
    }

    private static boolean isProHuawei() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_PRO_HUAWEI", metaData);
    }

    public static boolean isNormalApp() {
        String metaData = ResourceUtils.getMetaDateFromName("WALL_TYPE");
        return TextUtils.equals("WALL_APP_NORMAL", metaData);
    }

    public static boolean isPayCharge() {
        return isGooglePlayChannel() || isHWOversea();
    }

    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }

    /**
     * 关键字高亮显示(此处特别处理了关键字的每一个字符匹配;若无此要求，可以去掉for循环)
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target, int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        for (int i = 0; i < target.length(); i++) {
            Pattern p = Pattern.compile(Character.toString(target.charAt(i)));
            Matcher m = p.matcher(text);
            while (m.find()) {
                span = new ForegroundColorSpan(color);// 需要重复！
                spannable.setSpan(span, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    private static int statusBarHeight = 0;

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0) return statusBarHeight;
        statusBarHeight = ScreenUtils.dip2px(20);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static Drawable getDrawableByName(String name, Context context) {
        try {
            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            return ContextCompat.getDrawable(context, resId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ColorDrawable(Color.TRANSPARENT);
        }
    }

    public static String getProcessName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public void setWallPaper(boolean isLock, Bitmap bitmap) {
        if (bitmap == null)
            return;
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(UtilsContextManager.getInstance().getApplication());
            if (isLock) {
                Class class1 = wallpaperManager.getClass();//获取类名
                Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);//获取设置锁屏壁纸的函数
                setWallPaperMethod.invoke(wallpaperManager, bitmap);//调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
                MToastHelper.showToast("锁屏壁纸设置成功");
            } else {
                wallpaperManager.setBitmap(bitmap);
                MToastHelper.showToast("壁纸设置成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
