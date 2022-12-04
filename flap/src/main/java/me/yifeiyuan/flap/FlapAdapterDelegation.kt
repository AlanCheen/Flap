package me.yifeiyuan.flap

import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.RecyclerView

/**
 * All the methods that Flap must be called by the Adapter.
 *
 * Created by 程序亦非猿 on 2022/12/4.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface FlapAdapterDelegation {

    fun onCreateViewHolder(adapter: RecyclerView.Adapter<*>, parent: ViewGroup, viewType: Int): Component<*>

    fun onBindViewHolder(
            adapter: RecyclerView.Adapter<*>,
            itemData: Any,
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>)


    fun getItemViewType(position: Int, itemData: Any): Int

    fun getItemId(position: Int, itemData: Any): Long

    fun onViewRecycled(adapter: RecyclerView.Adapter<*>, component: Component<*>)

    fun onFailedToRecycleView(adapter: RecyclerView.Adapter<*>, component: Component<*>): Boolean

    fun onViewAttachedToWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>)
    fun onViewDetachedFromWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>)

    fun onAttachedToRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView)
    fun onDetachedFromRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView)
}