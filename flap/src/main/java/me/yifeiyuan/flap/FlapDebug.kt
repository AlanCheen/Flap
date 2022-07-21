package me.yifeiyuan.flap

import android.util.Log
import androidx.annotation.RestrictTo

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
object FlapDebug {

    private const val TAG = "FlapDebug"

    var isDebug = false

    fun e(tag: String, msg: String?, tr: Throwable?=null) {
        if (isDebug) {
            Log.e("$TAG-$tag", msg, tr)
        }
    }

    fun d(tag: String, msg: String?) {
        if (isDebug) {
            Log.d("$TAG-$tag", msg)
        }
    }

    fun w(tag: String, msg: String?) {
        if (isDebug) {
            Log.w("$TAG-$tag", msg)
        }
    }
}