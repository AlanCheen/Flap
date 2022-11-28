package me.yifeiyuan.flap.dsl.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.dsl.DslComponent

/**
 * 支持  AdapterDelegate ViewBinding DSL 功能
 * Created by 程序亦非猿 on 2022/9/20.
 * @since 3.1.3
 */
inline fun <reified T, reified V : ViewBinding> adapterDelegateViewBinding(
        noinline viewBinding: (layoutInflater: LayoutInflater, parent: ViewGroup) -> V,
        itemViewType: Int = 0,
        noinline isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == T::class.java },
        itemId: Long = RecyclerView.NO_ID,
        noinline componentInitializer: ViewBindingDslComponent<T, V>.() -> Unit): ViewBindingDslAdapterDelegate<T, V> {
    return ViewBindingDslAdapterDelegate(T::class.java, viewBinding, itemViewType, itemId, isDelegateFor = isDelegateFor, block = componentInitializer)
}

/**
 * 支持  AdapterDelegate ViewBinding DSL 功能
 * Created by 程序亦非猿 on 2022/9/20.
 * @since 3.1.3
 */
class ViewBindingDslAdapterDelegate<T, V : ViewBinding>(
        private var modelClass: Class<T>,
        private var viewBinding: (layoutInflater: LayoutInflater, parent: ViewGroup) -> V,
        private var itemViewType: Int,
        private var itemId: Long = RecyclerView.NO_ID,
        private var isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == modelClass },
        private var block: ViewBindingDslComponent<T, V>.() -> Unit,
) : AdapterDelegate<T, ViewBindingDslComponent<T, V>> {

    override fun isDelegateFor(model: Any): Boolean {
        return isDelegateFor.invoke(model)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewBindingDslComponent<T, V> {
        val binding = viewBinding(inflater, parent)
        val component = ViewBindingDslComponent<T, V>(binding)
        block.invoke(component)
        return component
    }

    override fun getItemId(model: Any, position: Int): Long {
        return itemId
    }

    override fun getItemViewType(model: Any): Int {
        return itemViewType
    }
}

/**
 * 支持  AdapterDelegate ViewBinding DSL 功能
 * Created by 程序亦非猿 on 2022/9/20.
 * @since 3.1.3
 */
class ViewBindingDslComponent<T, V : ViewBinding>(val binding: V) : DslComponent<T>(binding.root)