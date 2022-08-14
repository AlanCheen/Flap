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
 * @since 3.0.0
 */
class EmptyViewHelper : OnAdapterDataChangedObserver() {

    /**
     * 设置空状态下需要展示的 View
     * 当 adapter.itemCount == 0 时会展示；
     * 当 adapter.itemCount != 0 会隐藏；
     * 注意：emptyView 必须是已经加入到布局中的，是有 parent 的
     */
    var emptyView: View? = null

    /**
     * 展示内容的 View
     */
    var contentView: RecyclerView? = null

    fun attachRecyclerView(targetRecyclerView: RecyclerView) {
        contentView = targetRecyclerView
        contentView?.adapter?.let {
            attachAdapter(it)
        }
    }

    fun detachRecyclerView() {
        contentView?.adapter?.let {
            detachAdapter(it)
        }
    }

    /**
     *
     * @param adapter RecyclerView.adapter
     * @param checkRightNow 是否立即检查是否展示 empty view
     */
    fun attachAdapter(adapter: RecyclerView.Adapter<*>, checkRightNow: Boolean = false) {
        try {
            adapter.registerAdapterDataObserver(this)
        } catch (e: Exception) {
            //ignore
        }

        if (checkRightNow) {
            maybeShowEmptyView()
        }
    }

    fun detachAdapter(adapter: RecyclerView.Adapter<*>) {
        try {
            adapter.unregisterAdapterDataObserver(this)
        } catch (e: Exception) {
            //ignore
        }
    }

    override fun onAdapterDataChanged() {
        maybeShowEmptyView()
    }

    private fun maybeShowEmptyView() {
        if (contentView != null && emptyView != null)
            if (contentView!!.adapter != null && contentView!!.adapter?.itemCount == 0) {
                emptyView?.visibility = View.VISIBLE
                contentView?.visibility = View.GONE
            } else {
                emptyView?.visibility = View.GONE
                contentView?.visibility = View.VISIBLE
            }
    }
}