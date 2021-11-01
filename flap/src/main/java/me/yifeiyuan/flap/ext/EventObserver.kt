package me.yifeiyuan.flap.ext

/**
 * Created by 程序亦非猿 on 2021/11/1.
 */
interface EventObserver {
    fun onEvent(event: Event<*>)
}