package me.yifeiyuan.flap.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.FlapDebug

/**
 * 只绘制间隔的 ItemDecoration
 * 适用于 LinearLayoutManager
 *
 * 如果要绘制颜色 请用：
 * @see LinearItemDecoration
 * @see SpaceItemDecoration : 适用于所有 LayoutManager ，更推荐
 *
 * @param space 间隔，单位像素
 * @param orientation 方向，默认垂直
 *
 * @see isIncludeLastItemBottomEdge 可以设置最后一个 item 的底部与 RecyclerView 之间是否需要加间隔
 * @see isIncludeFirstItemTopEdge 可以设置第一个 item 的顶部与 RecyclerView 之间是否需要加间隔
 *
 * Created by 程序亦非猿 on 2022/8/19.
 * @since 3.0.4
 */
class LinearSpaceItemDecoration(var space: Int, var orientation: Int = RecyclerView.VERTICAL) : RecyclerView.ItemDecoration() {

    companion object {
        private const val TAG = "LinearSpaceItem"
    }

    /**
     * 第一个 item 的顶部与 RecyclerView 之间是否需要加间隔
     */
    private var isIncludeFirstItemTopEdge = false

    /**
     * 最后一个 item 的底部与 RecyclerView 之间是否需要加间隔
     */
    private var isIncludeLastItemBottomEdge = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition

        val adapterSize = parent.adapter?.itemCount ?: 0

        val layoutManager = parent.layoutManager

        when (layoutManager) {

            is LinearLayoutManager -> {
                val isFirst = position == 0
                val isLast = position == adapterSize - 1

                when (layoutManager.orientation) {

                    RecyclerView.VERTICAL -> {
                        outRect.top = if (isFirst && isIncludeFirstItemTopEdge) space else outRect.top
                        outRect.right = outRect.right
                        outRect.bottom = if ((!isLast || isIncludeLastItemBottomEdge)) space else outRect.bottom
                        outRect.left = outRect.left
                    }

                    RecyclerView.HORIZONTAL -> {
                        outRect.top = outRect.top
                        outRect.right = if ((!isLast || isIncludeLastItemBottomEdge)) space else outRect.right
                        outRect.bottom = outRect.bottom
                        outRect.left = if (isFirst && isIncludeFirstItemTopEdge) space else outRect.left
                    }
                }
            }
            else -> {
                FlapDebug.d(TAG, "LinearSpaceItemDecoration ：LayoutManager 非 LinearLayoutManager")
            }
        }
    }

    fun withFirstItemTopEdge(enable: Boolean): LinearSpaceItemDecoration {
        isIncludeFirstItemTopEdge = enable
        return this
    }

    fun withLastItemBottomEdge(enable: Boolean): LinearSpaceItemDecoration {
        isIncludeLastItemBottomEdge = enable
        return this
    }
}