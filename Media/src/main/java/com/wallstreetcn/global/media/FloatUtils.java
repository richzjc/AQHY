package com.wallstreetcn.global.media;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;

import com.micker.helper.ResourceUtils;
import com.micker.helper.SharedPrefsUtil;
import com.micker.helper.SmartFloatUtil;
import com.micker.helper.system.EquipmentUtils;


public class FloatUtils {

    public static boolean requireFloatVideo(Context context) {
        if (EquipmentUtils.checkCanDrawOverlays(context)) {
            return true;
        }else {
            AlertDialog.Builder ab = new AlertDialog.Builder(context)
                    .setMessage(ResourceUtils.getResStringFromId(R.string.live_room_open_overlay_window))
                    .setPositiveButton(ResourceUtils.getResStringFromId(R.string.live_room_to_set_up), ((dialog, which) -> {
                        try {
                            SmartFloatUtil.requestFloatPermission(context);
                            SharedPrefsUtil.save("FloatVideo", true);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            SharedPrefsUtil.save("FloatVideo", true);
                        }
                    }))
                    .setNegativeButton(ResourceUtils.getResStringFromId(R.string.live_room_cancel_text), (dialog, which) -> SharedPrefsUtil.save("FloatVideo", true));
            ab.setCancelable(false);
            ab.show();
            return false;
        }
    }
}
