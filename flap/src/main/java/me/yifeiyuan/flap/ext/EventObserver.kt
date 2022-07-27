package me.yifeiyuan.flap.ext

/**
 * Created by 程序亦非猿 on 2021/11/1.
 * todo 加泛型参数，加函数式调用
 */
interface EventObserver {
    fun onEvent(event: Event<*>)
}

class EventObserverWrapper<T>(var block: (Event<T>) -> Unit) : EventObserver {
    override fun onEvent(event: Event<*>) {
        block(event as Event<T>)
    }
}

