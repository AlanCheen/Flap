package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flapdev.R

/**
 *
 * 高度为 0 的组件
 * Created by 程序亦非猿 on 2022/8/4.
 */

class ZeroHeightModel

fun createZeroHeightComponentDelegate() = adapterDelegate<ZeroHeightModel>(R.layout.component_zero_height) {
    onBind { model ->

    }
}