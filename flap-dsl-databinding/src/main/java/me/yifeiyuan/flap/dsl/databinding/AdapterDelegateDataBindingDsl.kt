package me.yifeiyuan.flap.dsl.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.dsl.DslComponent

/**
 * Created by 程序亦非猿 on 2022/9/21.
 */

/**
 * ViewDataBinding?
 * 支持  AdapterDelegate ViewBinding DSL 功能
 * Created by 程序亦非猿 on 2022/9/20.
 * @since 3.1.3
 */
inline fun <reified T, reified D : ViewDataBinding> adapterDelegateDataBinding(
        @LayoutRes layoutId: Int,
        noinline dataBinding: ((layoutInflater: LayoutInflater, parent: ViewGroup) -> D) = { l, p -> DataBindingUtil.inflate(l, layoutId, p, false) },
        noinline isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == T::class.java },
        itemId: Long = RecyclerView.NO_ID,
        noinline componentInitializer: DataBindingDslComponent<T, D>.() -> Unit): DataBindingDslAdapterDelegate<T, D> {
    return DataBindingDslAdapterDelegate(T::class.java, dataBinding, layoutId, itemId, isDelegateFor, componentInitializer)
}

/**
 * 支持  AdapterDelegate ViewBinding DSL 功能
 * Created by 程序亦非猿 on 2022/9/20.
 * @since 3.1.3
 */
class DataBindingDslAdapterDelegate<T, D : ViewDataBinding>(
        private var modelClass: Class<T>,
        private var dataBinding: ((layoutInflater: LayoutInflater, parent: ViewGroup) -> D) = { l, p -> DataBindingUtil.inflate(l, layoutId, p, false) },
        @LayoutRes private var layoutId: Int,
        private var itemId: Long = RecyclerView.NO_ID,
        private var isDelegateFor: ((model: Any) -> Boolean) = { m -> m.javaClass == modelClass },
        private var block: DataBindingDslComponent<T, D>.() -> Unit,
) : AdapterDelegate<T, DataBindingDslComponent<T, D>> {

    override fun delegate(model: Any): Boolean {
        return isDelegateFor.invoke(model)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): DataBindingDslComponent<T, D> {
        val viewDataBinding = dataBinding.invoke(inflater, parent)
        val component = DataBindingDslComponent<T, D>(viewDataBinding)
        block.invoke(component)
        return component
    }

    override fun getItemId(model: Any, position: Int): Long {
        return itemId
    }

    override fun getItemViewType(model: Any): Int {
        return layoutId
    }
}

/**
 * 支持  AdapterDelegate DataBinding DSL 功能
 * Created by 程序亦非猿 on 2022/9/20.
 * @since 3.1.3
 */
class DataBindingDslComponent<T, V : ViewDataBinding>(val binding: V) : DslComponent<T>(binding.root)