package me.yifeiyuan.flap.delegate

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 *
 * LayoutAdapterDelegate DSL 支持
 *
 * Created by 程序亦非猿 on 2022/8/4.
 * @since 3.0.0
 */


class LayoutAdapterDelegateBuilder<Model>(private var modelClass: Class<*>, var layoutId: Int = 0) {

    /**
     * 简单参数的 onBind
     */
    private var binder: (Component<Model>.(model: Model) -> Unit)? = null

    /**
     * 更多参数的 onBind
     */
    private var binder2: (Component<Model>.(model: Model, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    private var onViewAttachedToWindow: (Component<Model>.() -> Unit)? = null

    private var onViewDetachedFromWindow: (Component<Model>.() -> Unit)? = null

    /**
     * 单击事件
     */
    private var onClickListener: (Component<Model>.(model: Model, position: Int) -> Unit)? = null

    /**
     * 长按事件
     */
    private var onLongClickListener: ((component: Component<Model>, model: Model, position: Int) -> Boolean)? = null

    private var itemId: Long = RecyclerView.NO_ID

    fun itemId(itemId: Long) {
        this.itemId = itemId
    }

    fun onBind(onBind: (Component<Model>.(model: Model) -> Unit)) {
        binder = onBind
    }

    fun onBind(onBind: (Component<Model>.(model: Model, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)) {
        binder2 = onBind
    }

    fun onClick(onclick: (Component<Model>.(model: Model, position: Int) -> Unit)) {
        onClickListener = onclick
    }

    fun onLongClick(onLongClick: (Component<Model>.(model: Model, position: Int) -> Boolean)) {
        onLongClickListener = onLongClick
    }

    fun onViewAttachedToWindow(onAttach: (Component<Model>.() -> Unit)) {
        onViewAttachedToWindow = onAttach
    }

    fun onViewDetachedFromWindow(onDetach: (Component<Model>.() -> Unit)) {
        onViewDetachedFromWindow = onDetach
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