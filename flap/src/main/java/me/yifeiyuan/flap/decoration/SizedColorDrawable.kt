package me.yifeiyuan.flap.decoration

import android.graphics.drawable.ColorDrawable

/**
 * 可以指定宽高的 ColorDrawable
 *
 * Created by 程序亦非猿 on 2022/8/18.
 * @since 3.0.4
 */
class SizedColorDrawable : ColorDrawable {

    private val height: Int

    private val width: Int

    constructor(color: Int, height: Int, width: Int) : super(color) {
        this.height = height
        this.width = width
    }

    constructor(color: Int, size: Int) : this(color, size, size)

    override fun getIntrinsicHeight(): Int {
        return height
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

}