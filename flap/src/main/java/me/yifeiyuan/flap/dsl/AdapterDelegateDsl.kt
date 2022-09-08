package me.yifeiyuan.flap.dsl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.AdapterDelegate


/**
 * 支持 AdapterDelegate DSL 功能
 *
 * @param layoutId 资源 id
 * @param isDelegateFor 代理关系
 * @param itemId
 * @param componentInitializer 初始化 DslComponent 设置
 *
 * @since 3.0.9
 */
inline fun <reified T> adapterDelegate(
        @LayoutRes layoutId: Int,
        noinline isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == T::class.java },
        itemId: Long = RecyclerView.NO_ID,
        noinline componentInitializer: DslComponent<T>.() -> Unit): DslAdapterDelegate<T> {
    return DslAdapterDelegate(T::class.java, layoutId, itemId, isDelegateFor = isDelegateFor, block = componentInitializer)
}

/**
 * 支持 DSL 化的 AdapterDelegate
 *
 * Created by 程序亦非猿 on 2022/9/1.
 * @since 3.0.9
 */
class DslAdapterDelegate<T>(
        private var modelClass: Class<T>,
        @LayoutRes private var layoutId: Int,
        private var itemId: Long = RecyclerView.NO_ID,
        private var isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == modelClass },
        private var block: DslComponent<T>.() -> Unit,
) : AdapterDelegate<T, DslComponent<T>> {

    override fun delegate(model: Any): Boolean {
        return isDelegateFor.invoke(model)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): DslComponent<T> {
        val view = inflater.inflate(viewType, parent, false)
        val component = DslComponent<T>(view)
        block.invoke(component)
        return component
    }

    override fun onBindViewHolder(component: Component<*>, data: Any, position: Int, payloads: List<Any>, adapter: FlapAdapter) {
        super.onBindViewHolder(component, data, position, payloads, adapter)
    }

    override fun getItemViewType(model: Any): Int {
        return layoutId
    }

    override fun getItemId(model: Any, position: Int): Long {
        return itemId
    }
}

/**
 * Created by 程序亦非猿 on 2022/9/1.
 *
 * @since 3.0.9
 */
class DslComponent<T>(view: View) : Component<T>(view) {

    /**
     * 更多参数的 onBind
     */
    private var onBind2: ((model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    /**
     * 简单参数的 onBind
     */
    private var onBind: ((model: T) -> Unit)? = null

    private var onViewAttachedToWindow: (() -> Unit)? = null
    private var onViewDetachedFromWindow: (() -> Unit)? = null

    private var onResume: (() -> Unit)? = null
    private var onPause: (() -> Unit)? = null
    private var onStop: (() -> Unit)? = null
    private var onDestroy: (() -> Unit)? = null

    private var onViewRecycled: (() -> Unit)? = null
    private var onFailedToRecycleView: (() -> Boolean)? = null

    /**
     * 单击事件
     */
    private var onClickListener: ((model: T, position: Int, adapter: FlapAdapter) -> Unit)? = null

    /**
     * 长按事件
     */
    private var onLongClickListener: ((model: T, position: Int, adapter: FlapAdapter) -> Boolean)? = null

    override fun onBind(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {

        if (onBind2 != null) {
            onBind2?.invoke(model, position, payloads, adapter)
        } else if (onBind != null) {
            onBind?.invoke(model)
        }

        if (onClickListener != null) {
            itemView.setOnClickListener {
                if (isClickable()) {
                    onClickListener?.invoke(model, position, adapter)
                }
            }
        } else {
            itemView.setOnClickListener(null)
        }

        if (onLongClickListener != null) {
            itemView.setOnLongClickListener {
                if (isLongClickable()) {
                    onLongClickListener!!.invoke(model, position, adapter)
                } else {
                    false
                }
            }
        } else {
            itemView.setOnLongClickListener(null)
        }
    }

    override fun onViewAttachedToWindow(flapAdapter: FlapAdapter) {
        super.onViewAttachedToWindow(flapAdapter)
        onViewAttachedToWindow?.invoke()
    }

    override fun onViewDetachedFromWindow(flapAdapter: FlapAdapter) {
        super.onViewDetachedFromWindow(flapAdapter)
        onViewDetachedFromWindow?.invoke()
    }

    override fun onViewRecycled(flapAdapter: FlapAdapter) {
        super.onViewRecycled(flapAdapter)
        onViewRecycled?.invoke()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        onPause?.invoke()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        onResume?.invoke()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        onStop?.invoke()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onDestroy?.invoke()
    }

    override fun onFailedToRecycleView(flapAdapter: FlapAdapter): Boolean {
        return onFailedToRecycleView?.invoke() ?: super.onFailedToRecycleView(flapAdapter)
    }

    fun onBind(onBind: ((model: T) -> Unit)) {
        this.onBind = onBind
    }

    fun onBind(onBind: ((model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)) {
        onBind2 = onBind
    }

    fun onClick(onclick: ((model: T, position: Int, adapter: FlapAdapter) -> Unit)) {
        onClickListener = onclick
    }

    fun onLongClick(onLongClick: ((model: T, position: Int, adapter: FlapAdapter) -> Boolean)) {
        onLongClickListener = onLongClick
    }

    fun onViewAttachedToWindow(onAttach: (() -> Unit)) {
        onViewAttachedToWindow = onAttach
    }

    fun onViewDetachedFromWindow(onDetach: (() -> Unit)) {
        onViewDetachedFromWindow = onDetach
    }

    fun onViewRecycled(block: (() -> Unit)) {
        onViewRecycled = block
    }

    fun onFailedToRecycleView(block: (() -> Boolean)) {
        onFailedToRecycleView = block
    }

    fun onResume(block: (() -> Unit)) {
        onResume = block
    }

    fun onPause(block: (() -> Unit)) {
        onPause = block
    }

    fun onStop(block: (() -> Unit)) {
        onStop = block
    }

    fun onDestroy(block: (() -> Unit)) {
        onDestroy = block
    }
}