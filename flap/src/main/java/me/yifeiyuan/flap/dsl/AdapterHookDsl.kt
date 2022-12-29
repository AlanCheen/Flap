@file:Suppress("unused")

package me.yifeiyuan.flap.dsl

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
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

    private var onPreCreateViewHolder: ((adapter: RecyclerView.Adapter<*>, viewType: Int) -> Unit)? = null
    private var onPostCreateViewHolder: ((adapter: RecyclerView.Adapter<*>, viewType: Int, component: Component<*>) -> Unit)? = null

    private var onViewAttachedToWindow: ((adapter: RecyclerView.Adapter<*>, component: Component<*>) -> Unit)? = null
    private var onViewDetachedFromWindow: ((adapter: RecyclerView.Adapter<*>, component: Component<*>) -> Unit)? = null

    private var onPreBindViewHolder: ((adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit)? = null
    private var onPostBindViewHolder: ((adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit)? = null

    fun onPreCreateViewHolder(block: (adapter: RecyclerView.Adapter<*>, viewType: Int) -> Unit) {
        onPreCreateViewHolder = block
    }

    fun onPostCreateViewHolder(block: (adapter: RecyclerView.Adapter<*>, viewType: Int, component: Component<*>) -> Unit) {
        onPostCreateViewHolder = block
    }

    fun onViewAttachedToWindow(block: (adapter: RecyclerView.Adapter<*>, component: Component<*>) -> Unit) {
        onViewAttachedToWindow = block
    }

    fun onViewDetachedFromWindow(block: (adapter: RecyclerView.Adapter<*>, component: Component<*>) -> Unit) {
        onViewDetachedFromWindow = block
    }

    fun onPreBindViewHolder(block: (adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit) {
        onPreBindViewHolder = block
    }

    fun onPostBindViewHolder(block: (adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) -> Unit) {
        onPostBindViewHolder = block
    }

    override fun onPreCreateViewHolder(adapter: RecyclerView.Adapter<*>, viewType: Int) {
        onPreCreateViewHolder?.invoke(adapter, viewType)
    }

    override fun onPostCreateViewHolder(adapter: RecyclerView.Adapter<*>, viewType: Int, component: Component<*>) {
        onPostCreateViewHolder?.invoke(adapter, viewType, component)
    }

    override fun onPreBindViewHolder(adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        onPreBindViewHolder?.invoke(adapter, component, data, position, payloads)
    }

    override fun onPostBindViewHolder(adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        onPostBindViewHolder?.invoke(adapter, component, data, position, payloads)
    }

    override fun onViewAttachedToWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        onViewAttachedToWindow?.invoke(adapter, component)
    }

    override fun onViewDetachedFromWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        onViewDetachedFromWindow?.invoke(adapter, component)
    }
}