package me.yifeiyuan.flap;

import android.util.Log;

import androidx.annotation.RestrictTo;

/**
 * Debug helper for Flap.
 * <p>
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 2018/12/13
 * @since 1.1
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class FlapDebug {

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

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }
}
