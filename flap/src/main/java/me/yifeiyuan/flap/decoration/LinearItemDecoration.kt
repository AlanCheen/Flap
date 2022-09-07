package me.yifeiyuan.flap.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * 可以绘制 Drawable 的 ItemDecoration，可以设置间隔的颜色和大小
 *
 * 如果不需要绘制任何颜色，可以使用：
 * @see LinearSpaceItemDecoration
 *
 * 代码实现参考内置的 DividerItemDecoration
 *
 * Created by 程序亦非猿 on 2022/8/18.
 *
 * @since 3.0.4
 */
class LinearItemDecoration : RecyclerView.ItemDecoration {

    /**
     * 第一个 item 的顶部与 RecyclerView 之间是否需要加间隔
     */
    private var isIncludeFirstItemTopEdge = false

    /**
     * 最后一个 item 的底部与 RecyclerView 之间是否需要加间隔
     */
    private var isIncludeLastItemBottomEdge = true

    var orientation = RecyclerView.VERTICAL

    var drawable: Drawable

    private val bounds = Rect()

    constructor(drawable: Drawable, orientation: Int = RecyclerView.VERTICAL) : super() {
        this.drawable = drawable
        this.orientation = orientation
    }

    /**
     * @param color 颜色 ,默认透明色 Color.TRANSPARENT
     * @param sizeInPx 尺寸，单位是像素
     */
    constructor(sizeInPx: Int, color: Int = Color.TRANSPARENT, orientation: Int = RecyclerView.VERTICAL) : this(SizedColorDrawable(color, sizeInPx), orientation)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        if (orientation == RecyclerView.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right,
                    parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom: Int = bounds.bottom + child.translationY.roundToInt()
            var top: Int = bottom - drawable.intrinsicHeight
            if (isIncludeFirstItemTopEdge && i == 0) {
                top = 0
            }
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        var top: Int
        var bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top,
                    parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            parent.layoutManager?.getDecoratedBoundsWithMargins(child, bounds)
            val right: Int = bounds.right + child.translationX.roundToInt()
            var left: Int = right - drawable.intrinsicWidth
            if (isIncludeFirstItemTopEdge && i == 0) {
                left = 0
            }
            bottom = child.height
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition

        if (orientation == RecyclerView.VERTICAL) {

            outRect.left = 0
            outRect.right = 0

            if (isIncludeFirstItemTopEdge && position == 0) {
                outRect.top = drawable.intrinsicHeight
            } else {
                outRect.top = 0
            }

            outRect.bottom = drawable.intrinsicHeight

            if (!isIncludeLastItemBottomEdge) {
                val isLastOne = parent.adapter != null && parent.adapter!!.itemCount > 0 && parent.adapter!!.itemCount - 1 == position
                if (isLastOne) {
                    outRect.bottom = 0
                }
            }
        } else {
            outRect.top = 0
            outRect.bottom = 0

            if (isIncludeFirstItemTopEdge && position == 0) {
                outRect.left = drawable.intrinsicWidth
            } else {
                outRect.left = 0
            }

            outRect.right = drawable.intrinsicWidth

            if (!isIncludeLastItemBottomEdge) {
                val isLastOne = parent.adapter != null && parent.adapter!!.itemCount > 0 && parent.adapter!!.itemCount - 1 == position
                if (isLastOne) {
                    outRect.right = 0
                }
            }
        }
    }

    fun withFirstItemTopEdge(enable: Boolean): LinearItemDecoration {
        isIncludeFirstItemTopEdge = enable
        return this
    }

    fun withLastItemBottomEdge(enable: Boolean): LinearItemDecoration {
        isIncludeLastItemBottomEdge = enable
        return this
    }
}