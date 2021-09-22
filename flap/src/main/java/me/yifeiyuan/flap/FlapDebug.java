package me.yifeiyuan.flap;

import android.util.Log;

import androidx.annotation.RestrictTo;

/**
 * Debug helper for Flap.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
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
