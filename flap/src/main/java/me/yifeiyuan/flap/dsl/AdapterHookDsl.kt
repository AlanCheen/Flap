@file:Suppress("unused")

package me.yifeiyuan.flap.dsl

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.hook.AdapterHook

/**
 * Created by 程序亦非猿 on 2022/9/5.
 *
 * @since 3.1.0
 */

/**
 * @since 3.1.0
 */
fun adapterHook(block: DslAdapterHook.() -> Unit): AdapterHook {
    return DslAdapterHook().apply(block)
}

/**
 * @since 3.1.0
 */
class DslAdapterHook : AdapterHook {

    private var onCreateViewHolderStart: ((adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) -> Unit)? = null
    private var onCreateViewHolderEnd: ((adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) -> Unit)? = null

    private var onViewAttachedToWindow: ((adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) -> Unit)? = null
    private var onViewDetachedFromWindow: ((adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) -> Unit)? = null

    private var onBindViewHolderStart: ((adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit)? = null
    private var onBindViewHolderEnd: ((adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit)? = null

    fun onCreateViewHolderStart(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) -> Unit) {
        onCreateViewHolderStart = block
    }

    fun onCreateViewHolderEnd(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) -> Unit) {
        onCreateViewHolderEnd = block
    }

    fun onViewAttachedToWindow(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) -> Unit) {
        onViewAttachedToWindow = block
    }

    fun onViewDetachedFromWindow(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) -> Unit) {
        onViewDetachedFromWindow = block
    }

    fun onBindViewHolderStart(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit) {
        onBindViewHolderStart = block
    }

    fun onBindViewHolderEnd(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit) {
        onBindViewHolderEnd = block
    }

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) {
        onCreateViewHolderStart?.invoke(adapter, delegate, viewType)
    }

    override fun onCreateViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) {
        onCreateViewHolderEnd?.invoke(adapter, delegate, viewType, component)
    }

    override fun onBindViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        onBindViewHolderStart?.invoke(adapter, delegate, component, data, position, payloads)
    }

    override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        onBindViewHolderEnd?.invoke(adapter, delegate, component, data, position, payloads)
    }

    override fun onViewAttachedToWindow(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) {
        onViewAttachedToWindow?.invoke(adapter, delegate, component)
    }

    override fun onViewDetachedFromWindow(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) {
        onViewDetachedFromWindow?.invoke(adapter, delegate, component)
    }
}