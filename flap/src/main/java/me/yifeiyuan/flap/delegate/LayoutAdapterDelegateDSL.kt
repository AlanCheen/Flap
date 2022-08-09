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


class LayoutAdapterDelegateBuilder<T>(private var modelClass: Class<*>, var layoutId: Int = 0) {

    /**
     * 简单参数的 onBind
     */
    private var onBind: (Component<T>.(model: T) -> Unit)? = null

    /**
     * 更多参数的 onBind
     */
    private var onBind2: (Component<T>.(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    private var onViewAttachedToWindow: (Component<T>.() -> Unit)? = null

    private var onViewDetachedFromWindow: (Component<T>.() -> Unit)? = null

    private var onViewRecycled: (Component<T>.(adapter: FlapAdapter) -> Unit)? = null
    private var onFailedToRecycleView: (Component<T>.(adapter: FlapAdapter) -> Boolean)? = null

    private var onResume: (Component<*>.() -> Unit)? = null
    private var onPause: (Component<*>.() -> Unit)? = null
    private var onStop: (Component<*>.() -> Unit)? = null
    private var onDestroy: (Component<*>.() -> Unit)? = null

    /**
     * 单击事件
     */
    private var onClickListener: (Component<T>.(model: T, position: Int) -> Unit)? = null

    /**
     * 长按事件
     */
    private var onLongClickListener: ((component: Component<T>, model: T, position: Int) -> Boolean)? = null

    private var itemId: Long = RecyclerView.NO_ID

    fun itemId(itemId: Long) {
        this.itemId = itemId
    }

    fun onBind(onBind: (Component<T>.(model: T) -> Unit)) {
        this.onBind = onBind
    }

    fun onBind(onBind: (Component<T>.(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)) {
        onBind2 = onBind
    }

    fun onClick(onclick: (Component<T>.(model: T, position: Int) -> Unit)) {
        onClickListener = onclick
    }

    fun onLongClick(onLongClick: (Component<T>.(model: T, position: Int) -> Boolean)) {
        onLongClickListener = onLongClick
    }

    fun onViewAttachedToWindow(onAttach: (Component<T>.() -> Unit)) {
        onViewAttachedToWindow = onAttach
    }

    fun onViewDetachedFromWindow(onDetach: (Component<T>.() -> Unit)) {
        onViewDetachedFromWindow = onDetach
    }

    fun onViewRecycled(block: (Component<T>.(adapter: FlapAdapter) -> Unit)) {
        onViewRecycled = block
    }

    fun onFailedToRecycleView(block: (Component<T>.(adapter: FlapAdapter) -> Boolean)) {
        onFailedToRecycleView = block
    }

    fun onResume(block: (Component<*>.() -> Unit)) {
        onResume = block
    }

    fun onPause(block: (Component<*>.() -> Unit)) {
        onPause = block
    }

    fun onStop(block: (Component<*>.() -> Unit)) {
        onStop = block
    }

    fun onDestroy(block: (Component<*>.() -> Unit)) {
        onDestroy = block
    }

    fun build(): LayoutAdapterDelegate<T, LayoutComponent<T>> {
        val config = LayoutAdapterDelegateConfig<T>()
        config.modelClass = modelClass
        config.onClickListener = onClickListener
        config.onLongClickListener = onLongClickListener
        config.onBind = onBind
        config.onBind2 = onBind2
        config.layoutId = layoutId
        config.itemId = itemId
        config.onViewAttachedToWindow = onViewAttachedToWindow
        config.onViewDetachedFromWindow = onViewDetachedFromWindow
        config.onViewRecycled = onViewRecycled
        config.onFailedToRecycleView = onFailedToRecycleView
        config.onResume = onResume
        config.onPause = onPause
        config.onStop = onStop
        config.onDestroy = onDestroy
        return LayoutAdapterDelegate(config)
    }
}

inline fun <reified T> makeDelegate(layoutId: Int, builder: LayoutAdapterDelegateBuilder<T>.() -> Unit): LayoutAdapterDelegate<T, LayoutComponent<T>> {
    return LayoutAdapterDelegateBuilder<T>(T::class.java, layoutId).apply(builder).build()
}