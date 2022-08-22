package me.yifeiyuan.flap.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * todo
 * Created by 程序亦非猿 on 2022/8/18.
 * @since 3.0.？
 */
class GridItemDecoration : RecyclerView.ItemDecoration {

    var drawable: Drawable? = null

    var orientation = RecyclerView.VERTICAL
    private val mBounds = Rect()

    constructor(drawable: Drawable) : super() {
        this.drawable = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager == null || drawable == null) {
            return
        }
        if (orientation == DividerItemDecoration.VERTICAL) {
            drawVertical(c, parent, state)
        } else {
            drawHorizontal(c, parent, state)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()

        val spanCount = getSpanCount(parent)

        val height = if (orientation == RecyclerView.VERTICAL) drawable!!.intrinsicHeight else drawable!!.intrinsicWidth
        val width = if (orientation != RecyclerView.VERTICAL) drawable!!.intrinsicHeight else drawable!!.intrinsicWidth

        val childCount = parent.childCount

        for (i in 0 until childCount) {

            val column = i % spanCount + 1

            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)

            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top: Int = bottom - drawable!!.intrinsicHeight
            val left = mBounds.left
//            (column - 1) * width / spanCount
            val right = mBounds.right
//            (spanCount - column) * width / spanCount
            drawable!!.setBounds(left, top, right, bottom)
            drawable!!.draw(c)

            // TODO: 2022/8/19
            drawable!!.setBounds(child.width - mBounds.left, 0, child.width, bottom)
            drawable!!.draw(c)
        }

        c.restore()
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val spanCount = getSpanCount(parent)

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition

        val height = if (orientation == RecyclerView.VERTICAL) drawable!!.intrinsicHeight else drawable!!.intrinsicWidth
        val width = if (orientation != RecyclerView.VERTICAL) drawable!!.intrinsicHeight else drawable!!.intrinsicWidth

        val column = position % spanCount + 1

        outRect.top = 0
        outRect.bottom = height
        outRect.left = (column - 1) * width / spanCount // 最左边的 left 不绘制
        outRect.right = (spanCount - column) * width / spanCount //最右边的 right 不绘制
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
}