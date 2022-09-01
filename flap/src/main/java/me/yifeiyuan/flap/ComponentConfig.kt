package me.yifeiyuan.flap

import me.yifeiyuan.flap.ext.SwipeDragHelper

/**
 * 组件的配置项抽象，可以控制部分功能的开关
 *
 * Created by 程序亦非猿 on 2022/8/31.
 *
 */
interface ComponentConfig {

    fun getSwipeFlags(): Int = SwipeDragHelper.FLAG_UN_SET

    fun getDragFlags(): Int = SwipeDragHelper.FLAG_UN_SET

    fun isSwipeEnable(): Boolean = true

    fun isDragEnable(): Boolean = true

    fun isClickable(): Boolean = true

    fun isLongClickable(): Boolean = true
}