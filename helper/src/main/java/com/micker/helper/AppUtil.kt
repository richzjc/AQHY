package com.micker.helper.utils

import android.app.ActivityManager
import android.content.Context
import com.micker.helper.UtilsContextManager

fun isAppOnForeground() : Boolean{
    val context = UtilsContextManager.getInstance().application
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val packageName = context.packageName
    val appProcesses = activityManager.runningAppProcesses ?: return false

    for (appProcess in appProcesses) {
        if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            return true
        }
    }
    return false
}