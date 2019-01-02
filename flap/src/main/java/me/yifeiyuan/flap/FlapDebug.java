package me.yifeiyuan.flap;

import android.util.Log;

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 *
 * Debug helper for Flap.
 *
 */
final class FlapDebug {

    private static final String TAG = "FlapDebug";

    private static boolean DEBUG = false;

    static void setDebug(boolean isDebugging) {
        DEBUG = isDebugging;
    }

    static void throwIfDebugging(Exception e) {
        if (DEBUG) {
            throw new RuntimeException(e);
        } else {
            Log.e(TAG, "FlapDebug : ", e);
        }
    }
}
