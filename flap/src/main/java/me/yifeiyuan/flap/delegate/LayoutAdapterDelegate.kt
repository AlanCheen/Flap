package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 * Created by 程序亦非猿 on 2022/9/2.
 */
class LayoutAdapterDelegate<T>(
        private var modelClass: Class<T>?,
        @LayoutRes
        private var layoutId: Int,
        private var itemId: Long = RecyclerView.NO_ID,
        private var isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == modelClass },
        private var block: Component<T>.(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit,
) : AdapterDelegate<T, Component<T>> {

    override fun delegate(model: Any): Boolean {
        return isDelegateFor.invoke(model)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): Component<T> {
        val view = inflater.inflate(viewType, parent, false)
        return LayoutComponent(view)
    }

    override fun onBindViewHolder(component: Component<*>, data: Any, position: Int, payloads: List<Any>, adapter: FlapAdapter) {
        block.invoke(component as Component<T>, data as T, position, payloads, adapter)
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