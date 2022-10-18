package me.yifeiyuan.flap.ext

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.ComponentConfig
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.widget.FlapIndexedStaggeredGridLayoutManager
import me.yifeiyuan.flap.widget.FlapStickyHeaders

/**
 * 一个支持设置 header 和 footer 的包装类 Adapter
 *
 * @see HeaderViewHolder
 * @see FooterViewHolder
 *
 * Created by 程序亦非猿 on 2022/7/31.
 * @since 3.0.0
 */
class HeaderFooterAdapter(var adapter: FlapAdapter) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), SwipeDragHelper.Callback, FlapStickyHeaders {

    private var headerView: View? = null
    private var footerView: View? = null

    init {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount, payload)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition + getHeaderCount(), toPosition + getHeaderCount())
            }

            override fun onStateRestorationPolicyChanged() {
                adapter.stateRestorationPolicy = stateRestorationPolicy
            }
        })
        setHasStableIds(adapter.hasStableIds())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return HeaderViewHolder(headerView!!)
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return FooterViewHolder(footerView!!)
        }
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        this.onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            // ignore
            when (attachingRecyclerView.layoutManager) {
                is GridLayoutManager -> {
                }
                else -> {
                }
            }
        } else {
            adapter.onBindViewHolder(holder as Component<*>, position - getHeaderCount(), payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (headerView != null) {
            if (position == 0) {
                return ITEM_VIEW_TYPE_HEADER
            } else if (position == itemCount - getHeaderCount() && footerView != null) {
                return ITEM_VIEW_TYPE_FOOTER
            } else {
                return adapter.getItemViewType(position - getHeaderCount())
            }
        } else {
            if (position == itemCount - getHeaderCount() && footerView != null) {
                return ITEM_VIEW_TYPE_FOOTER
            }
            return adapter.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        return getHeaderCount() + adapter.itemCount + getFooterCount()
    }

    fun getHeaderCount(): Int {
        return if (headerView == null) 0 else 1
    }

    fun getFooterCount(): Int {
        return if (footerView == null) 0 else 1
    }

    lateinit var attachingRecyclerView: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachingRecyclerView = recyclerView
        adapter.onAttachedToRecyclerView(recyclerView)

        when (recyclerView.layoutManager) {
            is GridLayoutManager -> {
                (recyclerView.layoutManager as GridLayoutManager).apply {
                    val preSpanSizeLookup = spanSizeLookup
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            if (this@HeaderFooterAdapter.isHeaderOrFooter(position)){
                                return spanCount
                            }else {
                                return preSpanSizeLookup?.getSpanSize(position) ?: 1
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
        } else {
            return adapter.onFailedToRecycleView(holder as Component<*>)
        }
        return super.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
            when (holder.itemView.layoutParams) {
                is GridLayoutManager.LayoutParams -> {
                }
                is StaggeredGridLayoutManager.LayoutParams -> {
                    val lp = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    lp.isFullSpan = true
                    holder.itemView.layoutParams = lp
                }
                is FlapIndexedStaggeredGridLayoutManager.LayoutParams -> {
                    val lp = (holder.itemView.layoutParams as FlapIndexedStaggeredGridLayoutManager.LayoutParams)
                    lp.isFullSpan = true
                    holder.itemView.layoutParams = lp
                }
            }
        } else {
            adapter.onViewAttachedToWindow(holder as Component<*>)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
        } else {
            adapter.onViewDetachedFromWindow(holder as Component<*>)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
        } else {
            adapter.onViewRecycled(holder as Component<*>)
        }
    }

    fun setupHeaderView(header: View, layoutParams: RecyclerView.LayoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)) {
        headerView = header
        headerView?.layoutParams = layoutParams
    }

    fun setupFooterView(footer: View, layoutParams: RecyclerView.LayoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)) {
        footerView = footer
        footerView?.layoutParams = layoutParams
    }

    fun isHeader(component: RecyclerView.ViewHolder): Boolean {
        return HeaderViewHolder::class.java == component.javaClass
    }

    fun isHeader(position: Int): Boolean {
        return getHeaderCount() > 0 && position < getHeaderCount()
    }

    fun isFooter(position: Int): Boolean {
        return getFooterCount() > 0 && itemCount == position + 1
    }

    fun isFooter(component: RecyclerView.ViewHolder): Boolean {
        return FooterViewHolder::class.java == component.javaClass
    }

    fun isHeaderOrFooter(component: RecyclerView.ViewHolder): Boolean = isHeader(component) or isFooter(component)

    fun isHeaderOrFooter(position: Int): Boolean = isHeader(position) or isFooter(position)

    override fun onSwiped(position: Int) {
        adapter.removeDataAt(position - getHeaderCount())
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.swapData(fromPosition - getHeaderCount(), toPosition - getHeaderCount())
    }

    override fun isStickyHeader(position: Int): Boolean {
        if (isHeaderOrFooter(position)) {
            return false
        }
        return adapter.isStickyHeader(position - getHeaderCount())
    }
}

const val ITEM_VIEW_TYPE_HEADER = 2123321000
const val ITEM_VIEW_TYPE_FOOTER = 2123321001

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view), ComponentConfig {
    override fun isSwipeEnabled(): Boolean {
        return false
    }

    override fun isDragEnabled(): Boolean {
        return false
    }

    override fun isClickable(): Boolean {
        return true
    }

    override fun isLongClickable(): Boolean {
        return true
    }
}

class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view), ComponentConfig {

    override fun isSwipeEnabled(): Boolean {
        return false
    }

    override fun isDragEnabled(): Boolean {
        return false
    }

    override fun isClickable(): Boolean {
        return true
    }

    override fun isLongClickable(): Boolean {
        return true
    }
}