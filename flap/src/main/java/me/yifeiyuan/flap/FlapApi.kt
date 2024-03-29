package me.yifeiyuan.flap

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.ext.EmptyViewHelper
import me.yifeiyuan.flap.ext.OnItemClickListener
import me.yifeiyuan.flap.ext.OnItemLongClickListener
import me.yifeiyuan.flap.hook.PreloadHook

/**
 * 抽象 Flap 类的 API。
 *
 * Created by 程序亦非猿 on 2022/11/3.
 * @since 3.3.0
 */
interface FlapApi : FlapRegistry, FlapAdapterDelegation {

    /**
     * Fire an event
     *
     * 通过 Adapter 发送事件
     *
     * @see observeEvent
     * @see observerEvents
     */
    fun <T> fireEvent(event: Event<T>)

    /**
     * 观察指定 eventName 的事件
     * @see fireEvent
     */
    fun <T> observeEvent(eventName: String, block: (Event<T>) -> Unit): FlapApi

    /**
     * Observe all the events.
     * 观察所有的事件
     */
    fun observerEvents(block: (Event<*>) -> Unit): FlapApi

    /**
     * 设置 Component 绑定的 LifecycleOwner
     * 会尝试去获取 recyclerView.context 作为 LifecycleOwner
     */
    fun withLifecycleOwner(lifecycleOwner: LifecycleOwner): FlapApi

    /**
     * Set use ComponentPool as the RecyclerViewPool or not.
     *
     * 设置是否使用 ComponentPool 作为缓存池
     */
    fun setComponentPoolEnable(enable: Boolean): FlapApi

    /**
     *
     * 预加载
     *
     * @see setPreloadEnable
     * @see PreloadHook
     */
    fun doOnPreload(offset: Int = 0, minItemCount: Int = 2, direction: Int = PreloadHook.SCROLL_DOWN, onPreload: () -> Unit): FlapApi

    /**
     * 设置是否启用预加载
     * 需要先调用 doOnPreload 开启才有效。
     */
    fun setPreloadEnable(enable: Boolean, direction: Int = PreloadHook.SCROLL_DOWN): FlapApi

    /**
     *
     * Set preload action is complete.
     *
     * 设置一次预加载行为完成。
     */
    fun setPreloadComplete(direction: Int = PreloadHook.SCROLL_DOWN)

    /**
     * OnItemClick
     *
     * 设置点击事件监听
     * @see doOnItemLongClick
     */
    fun doOnItemClick(onItemClick: OnItemClickListener?): FlapApi

    /**
     * OnItemLongClick
     *
     * 设置长按事件监听
     * @see doOnItemClick
     */
    fun doOnItemLongClick(onItemLongClick: OnItemLongClickListener?): FlapApi

    /**
     * Setup a view that will display when data set is empty.
     */
    fun withEmptyView(emptyView: View?): FlapApi

    fun getEmptyViewHelper(): EmptyViewHelper

    /**
     * Set a ParamProvider that provide extra param.
     */
    fun withParamProvider(provider: (key: String) -> Any?): FlapApi

    /**
     *
     * Get extra param from ParamProvider
     *
     * @see withParamProvider
     *
     * 提供 Component 从 Adapter 获取参数的方法
     *
     * @return key 对应的参数，如果类型不匹配，则会为 null
     */
    fun <P> getParam(key: String): P?

    /**
     * Set activity context.
     *
     * @see getActivity
     * @see Flap.inflateWithApplicationContext
     */
    fun withActivity(activity: Activity): FlapApi

    /**
     * Set a custom layoutInflater that will be used to inflate itemView.
     */
    fun withLayoutInflater(layoutInflater: LayoutInflater): FlapApi

    /**
     * @see withActivity
     * @see Flap.inflateWithApplicationContext
     * @return activity context
     */
    fun <T : Activity> getActivity(): T

    /**
     * Set a custom FlapRuntime
     */
    fun withRuntime(runtime: FlapRuntime): FlapApi

    fun <T : FlapRuntime> getRuntime(): T
}