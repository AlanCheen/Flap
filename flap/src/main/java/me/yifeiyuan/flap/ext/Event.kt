package me.yifeiyuan.flap.ext

/**
 * Created by 程序亦非猿 on 2021/9/30.
 */
class Event<T>(val eventName: String, var arg: T?) {
    var onError: (() -> Unit)? = null
    var onSuccess: (() -> Unit)? = null
    var onResult: (() -> Unit)? = null
}