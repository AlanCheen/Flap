package me.yifeiyuan.flapdev

import android.util.Log

/**
 * Created by 程序亦非猿 on 2022/7/17.
 */
object Logger {

    private const val TAG = "FLogger"

    fun d(tag: String, msg: String) {
        Log.d("$TAG-$tag", msg)
    }

    fun d(msg: String) {
        Log.d(TAG, msg)
    }
}