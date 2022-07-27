package me.yifeiyuan.flap.ext

/**
 * Created by 程序亦非猿 on 2021/11/1.
 */
interface EventObserver {
    fun onEvent(event: Event<*>)
}

/**
 * 包装成函数调用
 */
internal class EventObserverWrapper<T>(var block: (Event<T>) -> Unit) : EventObserver {
    override fun onEvent(event: Event<*>) {
        block(event as Event<T>)
    }
}
