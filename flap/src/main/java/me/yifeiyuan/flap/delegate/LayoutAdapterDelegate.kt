package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 *
 * LayoutAdapterDelegate 是为了降低创建 Component 和 自定义 AdapterDelegate 带来的使用成本而创建的。
 *
 * 使用 LayoutAdapterDelegate 的好处：
 * 1. 不需要写 Component
 * 2. 不需要写 AdapterDelegate
 * 3. 有 DSL 支持，更方便
 *
 * 使用 LayoutAdapterDelegate 必须要保证的情况：
 * 1. Model 与 LayoutAdapterDelegate 是一对一的关系
 * 2. layoutId 可以当做 itemViewType 直接用
 *
 * Created by 程序亦非猿 on 2021/10/27.
 * @since 3.0.0
 */
class LayoutAdapterDelegate<T, C : LayoutComponent<T>> : AdapterDelegate<T, C> {

    var config: LayoutAdapterDelegateConfig<T> = LayoutAdapterDelegateConfig()

    /**
     * 最简单的构造，只关心简单的 onBind
     */
    constructor(modelClass: Class<T>, layoutId: Int, binder: Component<T>.(model: T) -> Unit) {
        config.modelClass = modelClass
        config.layoutId = layoutId
        config.binder = binder
    }

    internal constructor(delegateConfig: LayoutAdapterDelegateConfig<T>) {
        config = delegateConfig
    }

    override fun delegate(model: Any): Boolean {
        return model.javaClass == config.modelClass
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): C {
        val view = inflater.inflate(viewType, parent, false)
        return LayoutComponent<T>(view) as C
    }

    override fun onBindViewHolder(component: Component<*>, data: Any, position: Int, payloads: List<Any>, adapter: FlapAdapter) {

        if (config.binder2 != null) {
            config.binder2?.invoke(component as Component<T>, data as T, position, payloads, adapter)
        } else if (config.binder != null) {
            config.binder?.invoke(component as Component<T>, data as T)
        }

        if (config.onClickListener != null) {
            component.itemView.setOnClickListener {
                config.onClickListener?.invoke(component as Component<T>, data as T, position)
            }
        } else {
            component.itemView.setOnClickListener(null)
        }

        if (config.onLongClickListener != null) {
            component.itemView.setOnLongClickListener {
                config.onLongClickListener!!.invoke(component as Component<T>, data as T, position)
            }
        } else {
            component.itemView.setOnLongClickListener(null)
        }
    }

    override fun getItemViewType(model: Any): Int {
        return config.layoutId
    }

    override fun getItemId(model: Any): Long {
        return config.itemId
    }

    override fun onViewAttachedToWindow(adapter: FlapAdapter, component: Component<*>) {
        super.onViewAttachedToWindow(adapter, component)
        config.onViewAttachedToWindow?.invoke(component as Component<T>)
    }

    override fun onViewDetachedFromWindow(adapter: FlapAdapter, component: Component<*>) {
        super.onViewDetachedFromWindow(adapter, component)
        config.onViewDetachedFromWindow?.invoke(component as Component<T>)
    }

    override fun onFailedToRecycleView(adapter: FlapAdapter, component: Component<*>): Boolean {
        return config.onFailedToRecycleView?.invoke(component as Component<T>, adapter) ?: super.onFailedToRecycleView(adapter, component)
    }

    override fun onViewRecycled(adapter: FlapAdapter, component: Component<*>) {
        super.onViewRecycled(adapter, component)
        config.onViewRecycled?.invoke(component as Component<T>, adapter)
    }
}

class LayoutComponent<T>(view: View) : Component<T>(view) {
    override fun onBind(model: T) {
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}

class LayoutAdapterDelegateConfig<T> {

    var modelClass: Class<*>? = null

    /**
     * 资源文件 layout id
     */
    var layoutId: Int = 0

    var itemId: Long = RecyclerView.NO_ID

    /**
     * 更多参数的 onBind
     */
    var binder2: (Component<T>.(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    /**
     * 简单参数的 onBind
     */
    var binder: (Component<T>.(model: T) -> Unit)? = null

    var onViewAttachedToWindow: (Component<T>.() -> Unit)? = null

    var onViewDetachedFromWindow: (Component<T>.() -> Unit)? = null
    var onResume: (Component<T>.() -> Unit)? = null
    var onPause: (Component<T>.() -> Unit)? = null
    var onStop: (Component<T>.() -> Unit)? = null
    var onDestroy: (Component<T>.() -> Unit)? = null

    var onViewRecycled: (Component<T>.(adapter: FlapAdapter) -> Unit)? = null
    var onFailedToRecycleView: (Component<T>.(adapter: FlapAdapter) -> Boolean)? = null

    /**
     * 单击事件
     */
    var onClickListener: (Component<T>.(model: T, position: Int) -> Unit)? = null

    /**
     * 长按事件
     */
    var onLongClickListener: (Component<T>.(model: T, position: Int) -> Boolean)? = null

}