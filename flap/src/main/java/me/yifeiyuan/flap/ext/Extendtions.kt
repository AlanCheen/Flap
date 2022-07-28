package me.yifeiyuan.flap.ext

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.hook.AdapterHook

/**
 * 一些扩展函数
 *
 * Created by 程序亦非猿 on 2022/7/29.
 * @since 3.0.0
 */

fun FlapAdapter.doOnCreateViewHolderStart(block: (delegate: AdapterDelegate<*, *>, viewType: Int) -> Unit) {
    registerAdapterHook(object : AdapterHook {
        override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) {
            block.invoke(delegate, viewType)
        }
    })
}

fun FlapAdapter.doOnCreateViewHolderEnd(block: (delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) -> Unit) {
    registerAdapterHook(object : AdapterHook {
        override fun onCreateViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) {
            block.invoke(delegate, viewType, component)
        }
    })
}

fun FlapAdapter.doOnBindViewHolderStart(block: (delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit) {
    registerAdapterHook(object : AdapterHook {
        override fun onBindViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
            block.invoke(delegate, component, data, position, payloads)
        }
    })
}

fun FlapAdapter.doOnBindViewHolderEnd(block: (delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit) {
    registerAdapterHook(object : AdapterHook {
        override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
            block.invoke(delegate, component, data, position, payloads)
        }
    })
}

fun FlapAdapter.doOnViewDetachedFromWindow(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) -> Unit) {
    registerAdapterHook(object : AdapterHook {
        override fun onViewDetachedFromWindow(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) {
            block.invoke(adapter, delegate, component)
        }
    })
}

fun FlapAdapter.doOnViewAttachedFromWindow(block: (adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) -> Unit) {
    registerAdapterHook(object : AdapterHook {
        override fun onViewAttachedToWindow(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) {
            block.invoke(adapter, delegate, component)
        }
    })
}

inline fun <reified T> Any.ifIs(block: T.() -> Unit) {
    if (this is T) {
        block.invoke(this)
    }
}