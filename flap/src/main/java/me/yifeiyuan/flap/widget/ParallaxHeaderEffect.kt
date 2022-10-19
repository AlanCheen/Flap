package me.yifeiyuan.flap.widget

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 程序亦非猿 on 2022/10/18.
 */

/**
 * 开启视差 Header 效果
 *
 * @param factor 视差因子，默认 0.5f ，当 1.0f 效果类似固定的 Header
 * @since 3.1.9
 */
fun RecyclerView.enableParallaxHeader(factor: Float = 0.5f) {
    addOnScrollListener(ParallaxHeaderHandler(factor))
}

internal class ParallaxHeaderHandler(private val factor: Float = 0.5f) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val child = recyclerView.getChildAt(0)
        if (child != null && recyclerView.getChildAdapterPosition(child) == 0) {
            child.translationY = -child.top * factor
        }
    }
}