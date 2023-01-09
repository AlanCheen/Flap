package me.yifeiyuan.flap.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.FlapApi
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.hook.IAdapterHookManager

/**
 * todo 改造，不默认放入Flap，在使用时绑定
 *
 * 当 FlapRecyclerView 内容为空时，帮助展示 EmptyView
 *
 * @see emptyView
 *
 * Created by 程序亦非猿 on 2022/6/16.
 * @since 3.0.0
 */
class EmptyViewHelper : OnAdapterDataChangedObserver(), AdapterHook {

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

    private fun attachRecyclerView(targetRecyclerView: RecyclerView, checkRightNow: Boolean = false) {
        contentView = targetRecyclerView
        contentView?.adapter?.let {
            attachAdapter(it, checkRightNow)
        }
    }

    private fun detachRecyclerView() {
        contentView?.adapter?.let {
            detachAdapter(it)
        }
    }

    /**
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

    override fun onAdapterAttachedToRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        attachRecyclerView(recyclerView, true)
    }

    override fun onAdapterDetachedFromRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        try {
            adapter.unregisterAdapterDataObserver(this)
        } catch (e: Exception) {
            //ignore
        }
        detachRecyclerView()
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