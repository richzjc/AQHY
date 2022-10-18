package com.micker.helper;

import android.util.Log;

public class TLog {

    public static final String LOG_TAG = "@WSCN";
    public static boolean DEBUG = false;

    private TLog() {

    }

    public static final void v(String log) {
        if (DEBUG)
            Log.v(LOG_TAG, log);
    }

    public static final void d(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void i(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void w(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }

    public static final void e(String log) {
        Log.e(LOG_TAG, "" + log);
    }
}