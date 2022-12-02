@file:Suppress("MemberVisibilityCanBePrivate", "unused", "LeakingThis")

package me.yifeiyuan.flap

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.ext.SwipeDragHelper
import me.yifeiyuan.flap.widget.FlapStickyHeaders
import java.util.*

/**
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
open class FlapAdapter(private val flap: Flap = Flap(), private val flapInitBlock: (FlapApi.() -> Unit)? = null) : RecyclerView.Adapter<Component<*>>(), SwipeDragHelper.Callback, FlapStickyHeaders, FlapApi by flap {

    companion object {
        private const val TAG = "FlapAdapter"
    }

    init {
        apply {
            flapInitBlock?.invoke(flap)
        }
    }

    private var data: MutableList<Any> = mutableListOf()

    // 暂时不需要
//    private fun getData(): List<Any> {
//        return Collections.unmodifiableList(data)
//    }

    /**
     * 在顶部添加数据
     */
    open fun addData(dataList: List<Any>) {
        data.addAll(dataList)
    }

    open fun addDataAndNotify(dataList: List<Any>, byNotifyDataSetChanged: Boolean = false) {
        data.addAll(0, dataList)
        if (byNotifyDataSetChanged) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(0, dataList.size)
        }
    }

    open fun setData(newDataList: MutableList<Any>) {
        data.clear()
        data.addAll(newDataList)
    }

    open fun <T : Any> setDataAndNotify(newDataList: MutableList<T>, byNotifyDataSetChanged: Boolean = false) {
        data.clear()
        data.addAll(newDataList)
        if (byNotifyDataSetChanged) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(0, newDataList.size)
        }
    }

    open fun appendData(appendDataList: MutableList<Any>) {
        data.addAll(appendDataList)
    }

    open fun appendDataAndNotify(appendDataList: MutableList<Any>, byNotifyDataSetChanged: Boolean = false) {
        data.addAll(appendDataList)
        if (byNotifyDataSetChanged) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(itemCount - appendDataList.size, appendDataList.size)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Component<*> {
        return flap.onCreateViewHolder(this, parent, viewType)
    }

    override fun onBindViewHolder(component: Component<*>, position: Int) {
        this.onBindViewHolder(component, position, mutableListOf())
    }

    override fun onBindViewHolder(
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        flap.onBindViewHolder(this, getItemData(position), component, position, payloads)
    }

    open fun getItemData(position: Int): Any {
        return data[position]
    }

    override fun getItemViewType(position: Int): Int {
        return flap.getItemViewType(position, getItemData(position))
    }

    override fun getItemId(position: Int): Long {
        return flap.getItemId(position, getItemData(position))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        flap.onAdapterAttachedToRecyclerView(this, recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        flap.onAdapterDetachedFromRecyclerView(this, recyclerView)
    }

    override fun onViewRecycled(component: Component<*>) {
        flap.onViewRecycled(this, component)
    }

    override fun onFailedToRecycleView(component: Component<*>): Boolean {
        return flap.onFailedToRecycleView(this, component)
    }

    override fun onViewAttachedToWindow(component: Component<*>) {
        flap.onViewAttachedToWindow(this, component)
    }

    override fun onViewDetachedFromWindow(component: Component<*>) {
        flap.onViewDetachedFromWindow(this, component)
    }

    fun insertDataAt(index: Int, element: Any, notify: Boolean = true) {
        this.data.add(index, element)
        if (notify) {
            notifyItemInserted(index)
        }
    }

    fun removeDataAt(index: Int, notify: Boolean = true) {
        data.removeAt(index)
        if (notify) {
            notifyItemRemoved(index)
        }
    }

    fun swapData(fromPosition: Int, toPosition: Int, notify: Boolean = true) {
        Collections.swap(data, fromPosition, toPosition)
        if (notify) {
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onSwiped(position: Int) {
        removeDataAt(position)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        swapData(fromPosition, toPosition)
    }

//    fun setupStickyHeaderView(stickyHeader: View) {
//    }
//
//    fun teardownStickyHeaderView(stickyHeader: View) {
//    }

    var stickyHeaderHandler: ((position: Int, itemData: Any) -> Boolean)? = null
    fun setupStickyHeaderHandler(block: (position: Int, itemData: Any) -> Boolean) {
        stickyHeaderHandler = block
    }

    override fun isStickyHeader(position: Int): Boolean {
        return stickyHeaderHandler?.invoke(position, getItemData(position)) ?: false
    }
}