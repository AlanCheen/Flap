package me.yifeiyuan.flap.ext

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.ComponentConfig
import me.yifeiyuan.flap.FlapAdapter

const val ITEM_VIEW_TYPE_HEADER = 2123321000
const val ITEM_VIEW_TYPE_FOOTER = 2123321001

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view), ComponentConfig {
    override fun isSwipeEnable(): Boolean {
        return false
    }

    override fun isDragEnable(): Boolean {
        return false
    }

    override fun isClickable(): Boolean {
        return false
    }

    override fun isLongClickable(): Boolean {
        return false
    }
}

class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view), ComponentConfig {

    override fun isSwipeEnable(): Boolean {
        return false
    }

    override fun isDragEnable(): Boolean {
        return false
    }

    override fun isClickable(): Boolean {
        return false
    }

    override fun isLongClickable(): Boolean {
        return false
    }
}

/**
 * 一个支持设置 header 和 footer 的包装类 Adapter
 *
 * Created by 程序亦非猿 on 2022/7/31.
 * @since 3.0.0
 */
class HeaderFooterAdapter(var adapter: FlapAdapter) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), SwipeDragHelper.Callback {

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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapter.onAttachedToRecyclerView(recyclerView)
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

    fun isFooter(component: RecyclerView.ViewHolder): Boolean {
        return FooterViewHolder::class.java == component.javaClass
    }

    override fun onItemDismiss(position: Int) {
        adapter.removeDataAt(position - getHeaderCount())
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        adapter.swapData(fromPosition - getHeaderCount(), toPosition - getHeaderCount())
    }
}