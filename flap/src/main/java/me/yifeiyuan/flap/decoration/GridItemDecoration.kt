package me.yifeiyuan.flapdev

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 程序亦非猿 on 2022/8/18.
 */
class GridItemDecoration : RecyclerView.ItemDecoration {

    var drawable: Drawable? = null

    constructor(drawable: Drawable) : super() {
        this.drawable = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
    }
}