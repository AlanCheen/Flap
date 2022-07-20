package me.yifeiyuan.flap.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * 当 FlapRecyclerView 内容为空时，帮助展示 EmptyView
 *
 * @see emptyView
 *
 * Created by 程序亦非猿 on 2022/6/16.
 */
internal class EmptyViewHelper(private val recyclerView: RecyclerView) : RecyclerView.AdapterDataObserver() {

    /**
     * 设置空状态下需要展示的 View
     * 当 adapter.itemCount == 0 时会展示；
     * 当 adapter.itemCount != 0 会隐藏；
     * 注意：emptyView 必须是已经加入到布局中的，是有 parent 的
     */
    var emptyView: View? = null
        set(value) {
            field = value
            checkEmptyState()
        }

    fun attachAdapter(adapter: RecyclerView.Adapter<*>) {
        adapter.registerAdapterDataObserver(this)
    }

    fun detachAdapter(adapter: RecyclerView.Adapter<*>) {
        adapter.registerAdapterDataObserver(this)
    }

    override fun onChanged() {
        super.onChanged()
        checkEmptyState()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkEmptyState()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkEmptyState()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        checkEmptyState()
    }

    private fun checkEmptyState() {
        emptyView?.let {
            if (recyclerView.adapter != null && recyclerView.adapter?.itemCount == 0) {
                it.visibility = RecyclerView.VISIBLE
                recyclerView.visibility = RecyclerView.GONE
            } else {
                it.visibility = RecyclerView.GONE
                recyclerView.visibility = RecyclerView.VISIBLE
            }
        }
    }

}