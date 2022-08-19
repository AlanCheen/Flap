package me.yifeiyuan.flap.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * 可以绘制 Drawable 的 ItemDecoration，可以设置间隔的颜色和大小
 *
 * 如果不需要绘制任何颜色，可以使用：
 * @see LinearSpaceItemDecoration
 *
 * Created by 程序亦非猿 on 2022/8/18.
 *
 * @since 3.0.4
 */
class LinearItemDecoration : DividerItemDecoration {

    constructor(context: Context, drawable: Drawable? = null, orientation: Int = VERTICAL) : super(context, orientation) {
        drawable?.let {
            setDrawable(it)
        }
    }

    /**
     * @param color 颜色 ,默认透明色 Color.parseColor("#00000000")
     * @param sizeInPx 尺寸，单位是像素
     */
    constructor(context: Context, sizeInPx: Int, color: Int = Color.parseColor("#00000000"), orientation: Int = VERTICAL) : super(context, orientation) {
        val colorDrawable = SizedColorDrawable(color, sizeInPx)
        setDrawable(colorDrawable)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }
}