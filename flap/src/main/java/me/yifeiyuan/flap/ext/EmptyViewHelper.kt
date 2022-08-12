package me.yifeiyuan.flap.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.NullPointerException

/**
 *
 * 当 FlapRecyclerView 内容为空时，帮助展示 EmptyView
 *
 * @see emptyView
 *
 * Created by 程序亦非猿 on 2022/6/16.
 * @since 3.0.0
 */
internal class EmptyViewHelper : OnAdapterDataChangedObserver() {

    /**
     * 设置空状态下需要展示的 View
     * 当 adapter.itemCount == 0 时会展示；
     * 当 adapter.itemCount != 0 会隐藏；
     * 注意：emptyView 必须是已经加入到布局中的，是有 parent 的
     */
    var emptyView: View? = null

    lateinit var recyclerView: RecyclerView

    fun attachRecyclerView(targetRecyclerView: RecyclerView, checkRightNow: Boolean = false) {
        recyclerView = targetRecyclerView
        if (targetRecyclerView.adapter == null) {
            throw NullPointerException("RecyclerView.adapter 为 null，请先给 RecyclerView 设置 Adapter")
        }
        targetRecyclerView.adapter?.registerAdapterDataObserver(this)
        if (checkRightNow) {
            maybeShowEmptyView()
        }
    }

    fun detachRecyclerView(targetRecyclerView: RecyclerView) {
        targetRecyclerView.adapter?.unregisterAdapterDataObserver(this)
    }

    override fun onDataChanged() {
        maybeShowEmptyView()
    }

    private fun maybeShowEmptyView() {
        emptyView?.let {
            if (!this::recyclerView.isInitialized) {
                return
            }
            if (recyclerView.adapter != null && recyclerView.adapter?.itemCount == 0) {
                it.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                it.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

}