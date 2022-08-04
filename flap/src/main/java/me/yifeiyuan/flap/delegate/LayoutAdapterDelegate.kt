package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 * todo
 *
 * LayoutAdapterDelegate 应对的情况：
 * 1. Model 与 LayoutAdapterDelegate 一对一
 * 2. 不想写 Component
 * 3. 不想写自定义 AdapterDelegate
 * 4. layoutId 可以当做 itemViewType 直接用
 *
 * Created by 程序亦非猿 on 2021/10/27.
 * @since 3.0.0
 */
class LayoutAdapterDelegate<T, C : LayoutComponent<T>> : AdapterDelegate<T, C> {

    var config: LayoutAdapterDelegateConfig<T> = LayoutAdapterDelegateConfig()

    companion object {

        inline fun <reified T> createLayoutAdapterDelegate(layoutId: Int, noinline binder: (component: Component<T>, model: T) -> Unit): LayoutAdapterDelegate<T, LayoutComponent<T>> {
            return LayoutAdapterDelegate(T::class.java, layoutId, binder)
        }
    }

    /**
     * 最简单的构造，只关心简单的 onBind
     */
    constructor(modelClass: Class<T>, layoutId: Int, binder: (component: Component<T>, model: T) -> Unit) {
        config.modelClass = modelClass
        config.layoutId = layoutId
        config.binder = binder
    }

    constructor(delegateConfig: LayoutAdapterDelegateConfig<T>) {
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
        config.onViewAttachedToWindow?.invoke(component)
    }

    override fun onViewDetachedFromWindow(adapter: FlapAdapter, component: Component<*>) {
        config.onViewDetachedFromWindow?.invoke(component)
    }
}

class LayoutComponent<T>(view: View) : Component<T>(view) {
    override fun onBind(model: T) {
    }
}

class LayoutAdapterDelegateConfig<Model> {

    var modelClass: Class<*>? = null

    /**
     * 资源文件 layout id
     */
    var layoutId: Int = 0

    var itemId: Long = RecyclerView.NO_ID

    /**
     * 更多参数的 onBind
     */
    var binder2: ((component: Component<Model>, model: Model, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    /**
     * 简单参数的 onBind
     */
    var binder: ((component: Component<Model>, model: Model) -> Unit)? = null

    var onViewAttachedToWindow: ((component: Component<*>) -> Unit)? = null

    var onViewDetachedFromWindow: ((component: Component<*>) -> Unit)? = null

    /**
     * 单击事件
     */
    var onClickListener: ((component: Component<Model>, model: Model, position: Int) -> Unit)? = null

    /**
     * 长按事件
     */
    var onLongClickListener: ((component: Component<Model>, model: Model, position: Int) -> Boolean)? = null

}