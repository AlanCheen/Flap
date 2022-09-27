package me.yifeiyuan.flapdev

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.FlapDebug
import me.yifeiyuan.flap.widget.FlapIndexedStaggeredGridLayoutManager

/**
 * SpaceItemDecoration 只绘制间隔，不能绘制 Drawable
 * 适用于：
 * - LinearLayoutManager
 * - GridLayoutManager
 * - StaggeredGridLayoutManager
 *
 * @param space : 间隔的大小，单位像素
 * @param isIncludeFirstRowTopEdge : 第一行 item 的顶部与 RecyclerView 之间是否需要加间隔
 * @param isIncludeLastRowBottomEdge : 最后一行 item 的底部与 RecyclerView 之间是否需要加间隔
 *
 * Created by 程序亦非猿 on 2022/8/23.
 *
 * @since 3.0.5
 */
class SpaceItemDecorationT(
        private val space: Int,
        private var isIncludeFirstRowTopEdge: Boolean = false,
        private var isIncludeLastRowBottomEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {

        val spacePx = space
        val adapterPos = parent.getChildAdapterPosition(view)
        val adapterSize = parent.adapter?.itemCount ?: 0

        when (val layoutManager = parent.layoutManager) {
            is FlapIndexedStaggeredGridLayoutManager ->{
                val layoutParams = view.layoutParams as FlapIndexedStaggeredGridLayoutManager.LayoutParams
                val isAxisEndSpan = (layoutParams.spanIndex + 1) % layoutManager.spanCount == 0
                val isCrossAxisEndSpan = adapterPos >= (adapterSize - layoutManager.spanCount)
                val dividedSpace = spacePx / 2

                when (layoutManager.orientation) {
                    FlapIndexedStaggeredGridLayoutManager.VERTICAL -> {
                        outRect.left = if (layoutParams.spanIndex == 0) 0 else dividedSpace
                        outRect.right = if (isAxisEndSpan) 0 else dividedSpace
                        outRect.top = if (adapterPos < layoutManager.spanCount && !isIncludeFirstRowTopEdge) 0 else dividedSpace
                        outRect.bottom = if (isCrossAxisEndSpan && !isIncludeLastRowBottomEdge) 0 else dividedSpace
                    }
                    FlapIndexedStaggeredGridLayoutManager.HORIZONTAL -> {
                        outRect.top = if (layoutParams.spanIndex == 0) 0 else dividedSpace
                        outRect.bottom = if (isAxisEndSpan) 0 else dividedSpace
                        outRect.left = if (adapterPos < layoutManager.spanCount && !isIncludeFirstRowTopEdge) 0 else dividedSpace
                        outRect.right = if (isCrossAxisEndSpan && !isIncludeLastRowBottomEdge) 0 else dividedSpace
                    }
                }
            }
            is StaggeredGridLayoutManager -> {
                val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

                val isAxisEndSpan = (layoutParams.spanIndex + 1) % layoutManager.spanCount == 0
                val isCrossAxisEndSpan = adapterPos >= (adapterSize - layoutManager.spanCount)
                val dividedSpace = spacePx / 2

                when (layoutManager.orientation) {
                    StaggeredGridLayoutManager.VERTICAL -> {
                        outRect.left = if (layoutParams.spanIndex == 0) 0 else dividedSpace
                        outRect.right = if (isAxisEndSpan) 0 else dividedSpace
                        outRect.top = if (adapterPos < layoutManager.spanCount && !isIncludeFirstRowTopEdge) 0 else dividedSpace
                        outRect.bottom = if (isCrossAxisEndSpan && !isIncludeLastRowBottomEdge) 0 else dividedSpace
                    }
                    StaggeredGridLayoutManager.HORIZONTAL -> {
                        outRect.top = if (layoutParams.spanIndex == 0) 0 else dividedSpace
                        outRect.bottom = if (isAxisEndSpan) 0 else dividedSpace
                        outRect.left = if (adapterPos < layoutManager.spanCount && !isIncludeFirstRowTopEdge) 0 else dividedSpace
                        outRect.right = if (isCrossAxisEndSpan && !isIncludeLastRowBottomEdge) 0 else dividedSpace
                    }
                }
            }
            is GridLayoutManager -> {
                val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams

                val isAxisEndSpan = (layoutParams.spanIndex + 1) % layoutManager.spanCount == 0
                val isCrossAxisEndSpan = adapterPos >= (adapterSize - layoutManager.spanCount)
                val dividedSpace = spacePx / 2

                when (layoutManager.orientation) {
                    GridLayoutManager.VERTICAL -> {
                        outRect.left = if (layoutParams.spanIndex == 0) 0 else dividedSpace
                        outRect.right = if (isAxisEndSpan) 0 else dividedSpace
                        outRect.top = if (adapterPos < layoutManager.spanCount && !isIncludeFirstRowTopEdge) 0 else dividedSpace
                        outRect.bottom = if (isCrossAxisEndSpan && !isIncludeLastRowBottomEdge) 0 else dividedSpace
                    }
                    GridLayoutManager.HORIZONTAL -> {
                        outRect.top = if (layoutParams.spanIndex == 0) 0 else dividedSpace
                        outRect.bottom = if (isAxisEndSpan) 0 else dividedSpace
                        outRect.left = if (adapterPos < layoutManager.spanCount && !isIncludeFirstRowTopEdge) 0 else dividedSpace
                        outRect.right = if (isCrossAxisEndSpan && !isIncludeLastRowBottomEdge) 0 else dividedSpace
                    }
                }
            }
            is LinearLayoutManager -> {
                val isFirst = adapterPos == 0
                val isLast = adapterPos == adapterSize - 1

                when (layoutManager.orientation) {

                    RecyclerView.VERTICAL -> {
                        outRect.top = if (isFirst && isIncludeFirstRowTopEdge) spacePx else outRect.top
                        outRect.right = outRect.right
                        outRect.bottom = if ((!isLast || isIncludeLastRowBottomEdge)) spacePx else outRect.bottom
                        outRect.left = outRect.left
                    }

                    RecyclerView.HORIZONTAL -> {
                        outRect.top = outRect.top
                        outRect.right = if ((!isLast || isIncludeLastRowBottomEdge)) spacePx else outRect.right
                        outRect.bottom = outRect.bottom
                        outRect.left = if (isFirst && isIncludeFirstRowTopEdge) spacePx else outRect.left
                    }
                }
            }
        }

        FlapDebug.d("SpaceItemDecoration", "getItemOffsets: $adapterPos,$outRect")
    }
}