package me.yifeiyuan.flap.event

/**
 * 在 FlapAdapter 与 Component 之间发送的事件
 *
 * @see me.yifeiyuan.flap.FlapAdapter.fireEvent
 * @see me.yifeiyuan.flap.FlapAdapter.observeEvent
 * @see EventObserver
 *
 * Created by 程序亦非猿 on 2021/9/30.
 * @since 3.0.0
 */
data class Event<T>(val eventName: String, var arg: T? = null, val onError: ((result: Any?) -> Unit)? = null, val onSuccess: ((result: Any?) -> Unit)? = null) {

    fun setEventResult(isSuccess: Boolean, result: Any? = null) {
        if (isSuccess) {
            onSuccess?.invoke(result)
        } else {
            onError?.invoke(result)
        }
    }
}