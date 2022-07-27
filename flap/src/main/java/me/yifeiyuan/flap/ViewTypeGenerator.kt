package me.yifeiyuan.flap

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by 程序亦非猿 on 2021/12/27.
 * @since 3.0.0
 */
object ViewTypeGenerator {
    private const val VIEW_TYPE_OFFSET = 20200522
    private val sNextGeneratedViewType = AtomicInteger(VIEW_TYPE_OFFSET)
    fun generateViewType(): Int {
        while (true) {
            val result = sNextGeneratedViewType.get()
            val newValue = result + 1
            if (sNextGeneratedViewType.compareAndSet(result, newValue)) {
                return result
            }
        }
    }
}