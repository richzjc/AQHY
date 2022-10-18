package com.micker.helper.system;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.micker.helper.R;
import com.micker.helper.Util;
import com.micker.helper.UtilsContextManager;

/**
 * Created by zhangyang on 16/7/6.
 */
public class EquipmentUtils {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileType() {
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本
     *
     * @return
     */
    public static String getMobileSystem() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getIMEI() {
        String imei;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            imei = "";
        }
        return imei;
    }

    public static String getAndroidId() {
        return Build.SERIAL;
    }

    private static Application getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }


    public static String getPackageName() {
        return getApplication().getPackageName();
    }

    public static String getVersionName() {
        try {
            PackageManager packManager = getApplication().getPackageManager();
            PackageInfo packInfo = packManager.getPackageInfo(
                    getApplication().getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode() {
        try {
            PackageManager packManager = getApplication().getPackageManager();
            PackageInfo packInfo = packManager.getPackageInfo(
                    getApplication().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getChannel() {
        return "";
    }

    public static String getBrandName() {
        return Build.BRAND;
    }

    public static String getModelName() {
        return Build.MODEL;
    }

    public static String getBuildName() {
        return Build.VERSION.RELEASE;
    }

    public static String getAppName() {
        return getApplication().getResources().getString(R.string.app_name);
    }

    public static String getAppHttpHeader() {
        String platform = Util.isPayment() ? "wscnPro" : "wscn";
        String header = platform + "|" + "Android|" + getVersionName() + "|" + getBuildName();
        header += "|" + getPackageName();
        return header;
    }


    public static boolean checkCanDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(drawOverlaysSettingsIntent);
                return false;
            }
        }
        return true;
    }

}
