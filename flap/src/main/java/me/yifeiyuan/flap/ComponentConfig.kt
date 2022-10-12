package me.yifeiyuan.flap

import androidx.recyclerview.widget.ItemTouchHelper
import me.yifeiyuan.flap.ext.SwipeDragHelper

/**
 * 组件的配置项抽象，可以控制部分功能的开关
 *
 * @see SwipeDragHelper
 * @see me.yifeiyuan.flap.ext.ItemClicksHelper
 *
 * Created by 程序亦非猿 on 2022/8/31.
 *
 * @since 3.0.8
 */
interface ComponentConfig {

    companion object{
        const val FLAGS_HORIZONTAL = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        const val FLAGS_VERTICAL = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        const val FLAGS_ALL_DIRECTIONS = FLAGS_HORIZONTAL or FLAGS_VERTICAL
    }

    /**
     * 支持滑动的方向
     */
    fun getSwipeFlags(): Int = SwipeDragHelper.FLAG_UN_SET

    /**
     * 支持拖动的方向
     */
    fun getDragFlags(): Int = SwipeDragHelper.FLAG_UN_SET

    /**
     * 是否可以滑动删除
     */
    fun isSwipeEnabled(): Boolean = true

    /**
     * 是否可以拖动
     */
    fun isDragEnabled(): Boolean = true

    /**
     * 是否可以点击
     */
    fun isClickable(): Boolean = true

    /**
     * 是否可以长按点击
     */
    fun isLongClickable(): Boolean = true
}