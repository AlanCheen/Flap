package me.yifeiyuan.flap;

import android.util.Log;

/**
 * Debug helper for Flap.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 2018/12/13
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
