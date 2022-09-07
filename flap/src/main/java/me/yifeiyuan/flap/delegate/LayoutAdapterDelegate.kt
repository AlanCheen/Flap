package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 *
 * LayoutAdapterDelegate 是为了降低创建 Component 和 自定义 AdapterDelegate 带来的使用成本而创建的。
 *
 * 使用 LayoutAdapterDelegate 必须要保证的情况：
 * 1. Model 与 LayoutAdapterDelegate 是一对一的关系
 * 2. layoutId 可以当做 itemViewType 直接用
 *
 * Created by 程序亦非猿 on 2021/10/27.
 * @since 3.0.0
 */
class LayoutAdapterDelegate<T>(
        private var modelClass: Class<T>?,
        @LayoutRes
        private var layoutId: Int,
        private var itemId: Long = RecyclerView.NO_ID,
        private var isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == modelClass },
        private var onBind: Component<T>.(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit,
) : AdapterDelegate<T, Component<T>> {

    override fun delegate(model: Any): Boolean {
        return isDelegateFor.invoke(model)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): Component<T> {
        val view = inflater.inflate(viewType, parent, false)
        return LayoutComponent(view)
    }

    override fun onBindViewHolder(component: Component<*>, data: Any, position: Int, payloads: List<Any>, adapter: FlapAdapter) {
        onBind.invoke(component as Component<T>, data as T, position, payloads, adapter)
    }

    override fun getItemViewType(model: Any): Int {
        return layoutId
    }

    override fun getItemId(model: Any, position: Int): Long {
        return itemId
    }
}

class LayoutComponent<T>(view: View) : Component<T>(view) {

    override fun onBind(model: T) {
    }
}