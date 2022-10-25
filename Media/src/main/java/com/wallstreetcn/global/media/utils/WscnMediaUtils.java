package com.wallstreetcn.global.media.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.micker.core.manager.AppManager;
import com.micker.helper.ResourceUtils;
import com.micker.helper.SharedPrefsUtil;
import com.micker.helper.Util;
import com.micker.helper.system.TDevice;
import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.VideoCacheManager;
import com.wallstreetcn.global.media.WscnMediaEntity;
import java.io.File;

public class WscnMediaUtils {

    private static final String VIDEO_CONFIG = "VIDEO_CONFIG_SP";

    public static void setVideoPlayPosition(WscnMediaEntity entity, long position) {
        String url = entity.getKey();
        if (TextUtils.isEmpty(url))
            return;
        SharedPrefsUtil.saveLong(VIDEO_CONFIG, url, position);
    }

    public static long getVideoPlayPosition(WscnMediaEntity entity) {
        String url = entity.getKey();
        if (TextUtils.isEmpty(url))
            return 0l;
        return SharedPrefsUtil.getLong(VIDEO_CONFIG, url, 0l);
    }

    public static void needWifiTips(DialogInterface.OnClickListener confirmListener) {
        if (Util.isConnectWIFI()) {
            confirmListener.onClick(null, 0);
            return;
        }
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity == null) {
            confirmListener.onClick(null, -1);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,
                R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage(ResourceUtils.getResStringFromId(R.string.live_room_mobile_tips))
                .setPositiveButton(ResourceUtils.getResStringFromId(R.string.live_room_play_text),
                        (dialog, which) -> {
                            if (confirmListener != null)
                                confirmListener.onClick(null, 0);
                            dialog.dismiss();
                        }).
                setNegativeButton(ResourceUtils.getResStringFromId(R.string.wall_global_cancel_text),
                        (dialog, which) -> {
                            if (confirmListener != null) {
                                confirmListener.onClick(dialog, -1);
                            }
                            dialog.dismiss();
                        })
                .show();
    }

    public static void openNoWifiTips(DialogInterface.OnClickListener confirmListener) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity == null) {
            confirmListener.onClick(null, -1);
            return;
        }
        if (!TDevice.isNetworkConnected()) {
            confirmListener.onClick(null, -1);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,
                R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage(ResourceUtils.getResStringFromId(R.string.wall_global_media_2g_play_tips))
//                .setPositiveButton(ResourceUtils.getResStringFromId(R.string.wall_global_sure_text),
                .setPositiveButton("是",
                        (dialog, which) -> {
                            dialog.dismiss();
                            if (confirmListener != null)
                                confirmListener.onClick(null, 0);
                        }).
                setNegativeButton("否",
//                setNegativeButton(ResourceUtils.getResStringFromId(R.string.wall_global_cancel_text),
                        (dialog, which) -> {
                            dialog.dismiss();
                            if (confirmListener != null) {
                                confirmListener.onClick(dialog, -1);
                            }
                        })
                .show();
    }

    public static boolean isDownload(String url) {
//        File local = new File(url);
//        if (local != null && local.exists()) {
//            return true;
//        }
//        DownloadModel downloadModel = DownloadManager.INSTANCE.getModel(url);
//        if (downloadModel != null && downloadModel.getState() == DownloadConstants.DOWNLOAD_FINISH) {
//            try {
//                String path = downloadModel.getSdCardFile();
//                File file = new File(path);
//                if (file != null && file.exists()) {
//                    if (!TextUtils.isEmpty(url) && url.endsWith(".m3u8")) {
//                        return true;
//                    } else if (downloadModel.getDownloadLength() == file.length()) {
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return false;
    }


    public static boolean offline(String url) {
        if (TextUtils.isEmpty(url))
            return false;
        if (isDownload(url)) return true;
        return VideoCacheManager.getProxy().isCached(url);
    }

    public static void cleanCache(String url) {
//        try {
//            DownloadModel downloadModel = DownloadManager.INSTANCE.getModel(url);
//            if (downloadModel != null && downloadModel.getState() == DownloadConstants.DOWNLOAD_FINISH) {
//                String path = downloadModel.getSdCardFile();
//                if (FileUtil.isFileExist(path)) {
//                    FileUtil.deleteFile(path);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
