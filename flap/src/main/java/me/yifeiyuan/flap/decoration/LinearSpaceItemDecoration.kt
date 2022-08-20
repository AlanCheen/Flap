package me.yifeiyuan.flap.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 只绘制间隔的 ItemDecoration
 * 适用于 LinearLayoutManager
 *
 * 如果要绘制颜色 请用：
 * @see LinearItemDecoration
 *
 * @param space 间隔，单位像素
 * @param orientation 方向，默认垂直
 *
 * @see isIncludeLastItemBottomSpace 可以设置最后一个 item 的底部与 RecyclerView 之间是否需要加间隔
 * @see isIncludeFirstItemTopSpace 可以设置第一个 item 的顶部与 RecyclerView 之间是否需要加间隔
 *
 * Created by 程序亦非猿 on 2022/8/19.
 * @since 3.0.4
 */
class LinearSpaceItemDecoration(var space: Int, var orientation: Int = RecyclerView.VERTICAL) : RecyclerView.ItemDecoration() {

    /**
     * 第一个 item 的顶部与 RecyclerView 之间是否需要加间隔
     */
    var isIncludeFirstItemTopSpace = false

    /**
     * 最后一个 item 的底部与 RecyclerView 之间是否需要加间隔
     */
    var isIncludeLastItemBottomSpace = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition

        if (orientation == RecyclerView.VERTICAL) {

            outRect.left = 0
            outRect.right = 0

            if (isIncludeFirstItemTopSpace && position == 0) {
                outRect.top = space
            } else {
                outRect.top = 0
            }

            outRect.bottom = space

            if (!isIncludeLastItemBottomSpace) {
                val isLastOne = parent.adapter != null && parent.adapter!!.itemCount > 0 && parent.adapter!!.itemCount - 1 == position
                if (isLastOne) {
                    outRect.bottom = 0
                }
            }
        } else {
            outRect.top = 0
            outRect.bottom = 0

            if (isIncludeFirstItemTopSpace && position == 0) {
                outRect.left = space
            } else {
                outRect.left = 0
            }

            outRect.right = space

            if (!isIncludeLastItemBottomSpace) {
                val isLastOne = parent.adapter != null && parent.adapter!!.itemCount > 0 && parent.adapter!!.itemCount - 1 == position
                if (isLastOne) {
                    outRect.right = 0
                }
            }
        }
    }
}