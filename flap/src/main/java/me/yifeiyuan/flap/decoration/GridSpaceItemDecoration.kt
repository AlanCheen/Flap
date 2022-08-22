package me.yifeiyuan.flap.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * GridSpaceItemDecoration 只绘制间隔，不能绘制 Drawable
 * 适用于 GridLayoutManager 和 StaggeredGridLayoutManager
 *
 * Created by 程序亦非猿 on 2022/8/18.
 * @since 3.0.4
 */
class GridSpaceItemDecoration(var space: Int, var orientation: Int = RecyclerView.VERTICAL) : RecyclerView.ItemDecoration() {

    /**
     * 如果 true，那么每个 item 的垂直方向的 space 就在顶部，表现为第一行的 item 和 RecyclerView 的最顶部有一个间隙
     * 反之则在底部
     */
    var isIncludeFirstRowTopEdge = true

    var isIncludeLastRowTopEdge = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val spanCount = getSpanCount(parent)

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition

        val column = position % spanCount + 1

        val height = space
        val width = space

        if (orientation == RecyclerView.VERTICAL) {
            outRect.top = if (isIncludeFirstRowTopEdge) height else 0
            outRect.bottom = if (isIncludeFirstRowTopEdge) 0 else height
            outRect.left = (column - 1) * width / spanCount // 最左边的 left 不绘制
            outRect.right = (spanCount - column) * width / spanCount //最右边的 right 不绘制
        } else {
            outRect.right = width
            outRect.left = if (isIncludeFirstRowTopEdge) width else 0
            outRect.top = space - column * space / spanCount
            outRect.bottom = (column + 1) * space / spanCount
        }
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = 1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    fun withFirstRowEdge(enable: Boolean): GridSpaceItemDecoration {
        isIncludeFirstRowTopEdge = enable
        return this
    }

    fun isLastRow(position: Int, parent: RecyclerView): Boolean {


        return false
    }
}