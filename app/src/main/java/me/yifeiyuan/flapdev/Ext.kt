package me.yifeiyuan.flapdev

import android.content.Context

/**
 * Created by 程序亦非猿 on 2022/8/19.
 */

fun Context.toPixel(dip: Int): Int {
    val scale: Float = resources.displayMetrics.scaledDensity
    return (dip * scale + 0.5).toInt()
}