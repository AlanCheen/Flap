package me.yifeiyuan.flap.delegate

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 * Created by 程序亦非猿 on 2022/8/4.
 */


class LayoutAdapterDelegateBuilder<Model>(private var modelClass: Class<*>, var layoutId: Int = 0) {

    private var itemId: Long = RecyclerView.NO_ID

    /**
     * 更多参数的 onBind
     */
    private var binder2: ((component: Component<Model>, model: Model, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    /**
     * 简单参数的 onBind
     */
    private var binder: ((component: Component<Model>, model: Model, position: Int) -> Unit)? = null

    private var onViewAttachedToWindow: ((component: Component<*>) -> Unit)? = null

    private var onViewDetachedFromWindow: ((component: Component<*>) -> Unit)? = null

    /**
     * 单击事件
     */
    private var onClickListener: ((component: Component<Model>, model: Model, position: Int) -> Unit)? = null

    /**
     * 长按事件
     */
    private var onLongClickListener: ((component: Component<Model>, model: Model, position: Int) -> Boolean)? = null

    fun onViewAttachedToWindow(onAttach: ((component: Component<*>) -> Unit)) {
        onViewAttachedToWindow = onAttach
    }

    fun onViewDetachedFromWindow(onDetach: ((component: Component<*>) -> Unit)) {
        onViewDetachedFromWindow = onDetach
    }

    fun onBind(onBind: ((component: Component<Model>, model: Model, position: Int) -> Unit)) {
        binder = onBind
    }

    fun onClick(onclick: ((component: Component<Model>, model: Model, position: Int) -> Unit)) {
        onClickListener = onclick
    }

    fun onLongClick(onLongClick: ((component: Component<Model>, model: Model, position: Int) -> Boolean)) {
        onLongClickListener = onLongClick
    }

    fun layoutId(layoutId: Int) {
        this.layoutId = layoutId
    }

    fun itemId(itemId: Long) {
        this.itemId = itemId
    }

    fun build(): LayoutAdapterDelegate<Model, LayoutComponent<Model>> {
        val config = LayoutAdapterDelegateConfig<Model>()
        config.modelClass = modelClass
        config.onClickListener = onClickListener
        config.onLongClickListener = onLongClickListener
        config.binder = binder
        config.binder2 = binder2
        config.layoutId = layoutId
        config.itemId = itemId
        config.onViewAttachedToWindow = onViewAttachedToWindow
        config.onViewDetachedFromWindow = onViewDetachedFromWindow
        return LayoutAdapterDelegate(config)
    }
}

inline fun <reified Model> makeDelegate(layoutId: Int, builder: LayoutAdapterDelegateBuilder<Model>.() -> Unit): LayoutAdapterDelegate<Model, LayoutComponent<Model>> {
    return LayoutAdapterDelegateBuilder<Model>(Model::class.java, layoutId).apply(builder).build()
}