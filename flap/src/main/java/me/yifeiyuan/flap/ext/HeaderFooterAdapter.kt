package me.yifeiyuan.flap.ext

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

const val ITEM_VIEW_TYPE_HEADER = 2123321000
const val ITEM_VIEW_TYPE_FOOTER = 2123321001

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

/**
 * 一个支持设置 header 和 footer 的包装类 Adapter
 *
 * Created by 程序亦非猿 on 2022/7/31.
 * @since 3.0.0
 */
class HeaderFooterAdapter<VH : RecyclerView.ViewHolder, A : RecyclerView.Adapter<VH>>(var adapter: A) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var headerView: View? = null
    private var footerView: View? = null

    init {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                notifyItemRangeChanged(positionStart, itemCount, payload)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onStateRestorationPolicyChanged() {
                super.onStateRestorationPolicyChanged()
                adapter.stateRestorationPolicy = stateRestorationPolicy
            }
        })
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
            adapter.onBindViewHolder(holder as VH, position - getHeaderCount(), payloads)
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
            return adapter.onFailedToRecycleView(holder as VH)
        }
        return super.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
        } else {
            adapter.onViewAttachedToWindow(holder as VH)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
        } else {
            adapter.onViewDetachedFromWindow(holder as VH)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemViewType == ITEM_VIEW_TYPE_HEADER || holder.itemViewType == ITEM_VIEW_TYPE_FOOTER) {
            //ignore
        } else {
            adapter.onViewRecycled(holder as VH)
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
}