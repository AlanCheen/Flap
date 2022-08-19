package me.yifeiyuan.flapdev

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DividerItemDecoration

/**
 * Created by 程序亦非猿 on 2022/8/18.
 */
class LinearItemDecoration : DividerItemDecoration {

    constructor(context: Context, drawable: Drawable? = null, orientation: Int = VERTICAL) : super(context, orientation) {
        drawable?.let {
            setDrawable(it)
        }
    }

    constructor(context: Context, orientation: Int = VERTICAL) : super(context, orientation)

    /**
     * @param color 颜色 , Color.parseColor("#ff0000")
     * @param sizeInPx 尺寸，单位是像素
     */
    constructor(context: Context, color: Int, sizeInPx: Int, orientation: Int = VERTICAL) : super(context, orientation) {
        val colorDrawable = SizeColorDrawable(color, sizeInPx)
        setDrawable(colorDrawable)
    }
}

class SizeColorDrawable(color: Int, private val size: Int) : ColorDrawable(color) {

    override fun getIntrinsicHeight(): Int {
        return size
    }

    override fun getIntrinsicWidth(): Int {
        return size
    }
}