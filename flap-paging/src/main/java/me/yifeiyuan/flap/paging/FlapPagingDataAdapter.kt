package me.yifeiyuan.flap.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.FlapApi
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.ext.SwipeDragHelper
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.IAdapterServiceManager
import me.yifeiyuan.flap.widget.FlapStickyHeaders

/**
 *
 * 支持 Paging
 * Created by 程序亦非猿 on 2022/11/7.
 *
 * @since 3.3.0
 */
class FlapPagingDataAdapter<T : Any>(private val flap: Flap = Flap(), diffCallback: DiffUtil.ItemCallback<T>, private val flapInitBlock: (FlapApi.() -> Unit)? = null) : PagingDataAdapter<T, Component<T>>(diffCallback), SwipeDragHelper.Callback, FlapStickyHeaders, FlapApi by flap {

    init {
        apply {
            flapInitBlock?.invoke(flap)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return flap.getItemViewType(position, getItemData(position) as Any)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Component<T> {
        return flap.onCreateViewHolder(this, parent, viewType) as Component<T>
    }

    override fun onBindViewHolder(component: Component<T>, position: Int) {
        this.onBindViewHolder(component, position, mutableListOf())
    }

    override fun onBindViewHolder(
            component: Component<T>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        flap.onBindViewHolder(this, getItemData(position) as Any, component, position, payloads)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        flap.onAttachedToRecyclerView(this, recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        flap.onDetachedFromRecyclerView(this, recyclerView)
    }

    var stickyHeaderHandler: ((position: Int, itemData: T?) -> Boolean)? = null

    fun setupStickyHeaderHandler(block: (position: Int, itemData: T?) -> Boolean) {
        stickyHeaderHandler = block
    }

    override fun isStickyHeader(position: Int): Boolean {
        return stickyHeaderHandler?.invoke(position, getItemData(position)) ?: false
    }

    fun getItemData(position: Int): T? {
        return peek(position)
    }
}