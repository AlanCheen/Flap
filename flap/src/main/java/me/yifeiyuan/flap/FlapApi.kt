package me.yifeiyuan.flap

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.ext.EmptyViewHelper
import me.yifeiyuan.flap.ext.OnItemClickListener
import me.yifeiyuan.flap.ext.OnItemLongClickListener
import me.yifeiyuan.flap.hook.PreloadHook

/**
 * Created by 程序亦非猿 on 2022/11/3.
 */
interface FlapApi {

    /**
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
     * 观察所有的事件
     */
    fun observerEvents(block: (Event<*>) -> Unit): FlapApi

    /**
     * 设置 Component 绑定的 LifecycleOwner
     * 会尝试去获取 recyclerView.context 作为 LifecycleOwner
     */
    fun withLifecycleOwner(lifecycleOwner: LifecycleOwner): FlapApi

    /**
     * 设置 Component 是否监听生命周期，默认开启
     */
    fun withLifecycleEnable(enable: Boolean): FlapApi

    /**
     * 设置是否使用 ComponentPool 作为缓存池
     */
    fun withComponentPoolEnable(enable: Boolean): FlapApi

    /**
     * 预加载
     *
     * @see PreloadHook
     */
    fun doOnPreload(offset: Int = 0, minItemCount: Int = 2, direction: Int = PreloadHook.SCROLL_DOWN, onPreload: () -> Unit): FlapApi

    /**
     * 设置是否启用预加载
     * 需要先调用 doOnPreload 开启才有效。
     */
    fun setPreloadEnable(enable: Boolean, direction: Int = PreloadHook.SCROLL_DOWN): FlapApi

    fun setPreloadComplete(direction: Int = PreloadHook.SCROLL_DOWN)

    /**
     * 设置点击事件监听
     * @see doOnItemLongClick
     */
    fun doOnItemClick(onItemClick: OnItemClickListener?): FlapApi

    /**
     * 设置长按事件监听
     * @see doOnItemClick
     */
    fun doOnItemLongClick(onItemLongClick: OnItemLongClickListener?): FlapApi

    fun withEmptyView(emptyView: View?): FlapApi

    fun getEmptyViewHelper():EmptyViewHelper

    fun setParamProvider(block: (key: String) -> Any?): FlapApi

    /**
     * 提供 Component 从 Adapter 获取参数的方法
     *
     * @return key 对应的参数，如果类型不匹配，则会为 null
     */
    fun <P> getParam(key: String): P?

    /**
     * @see FlapAdapter.inflateWithApplicationContext
     * @return activity context
     */
    fun getActivityContext(): Context
}