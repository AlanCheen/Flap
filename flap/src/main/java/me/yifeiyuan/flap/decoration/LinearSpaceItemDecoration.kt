package me.yifeiyuan.flap.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 只绘制间隔的 ItemDecoration，适用于 LinearLayoutManager
 *
 * @param space 间隔，单位像素
 * @param orientation 方向，默认垂直
 *
 * Created by 程序亦非猿 on 2022/8/19.
 * @since 3.0.4
 */
class LinearSpaceItemDecoration(var space: Int, var orientation: Int = RecyclerView.VERTICAL) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(0, 0, 0, space)
        } else {
            outRect.set(0, 0, space, 0)
        }
    }
}