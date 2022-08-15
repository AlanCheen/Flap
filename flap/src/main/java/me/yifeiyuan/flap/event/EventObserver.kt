package me.yifeiyuan.flap.event

/**
 *
 * @see Event
 *
 * Created by 程序亦非猿 on 2021/11/1.
 * @since 3.0.0
 */
interface EventObserver {
    fun onEvent(event: Event<*>)
}

/**
 * 包装成函数调用
 */
internal class EventObserverWrapper<T>(val block: (Event<T>) -> Unit) : EventObserver {
    override fun onEvent(event: Event<*>) {
        block(event as Event<T>)
    }
}
