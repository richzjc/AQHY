package com.micker.global.util;

import android.app.Activity;
import com.micker.global.callback.PermissionCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PermissionUtils {
    public static void requestPermission(Activity activity, PermissionCallback permissionCallback, String ... permissions){
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> throwable.printStackTrace())
                .doOnComplete(() -> {
                    if(permissionCallback != null)
                        permissionCallback.requestPermissSuccess();
                }).subscribe();
    }
}
