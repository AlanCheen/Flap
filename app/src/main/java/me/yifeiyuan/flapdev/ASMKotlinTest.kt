package me.yifeiyuan.flapdev

import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.apt.delegates.SimpleImageComponent2Delegate
import me.yifeiyuan.flapdev.components.simpleimage.SimpleImageComponentDelegate

/**
 * Created by 程序亦非猿 on 2021/9/23.
 */
class ASMKotlinTest {

    fun flap(flap: Flap) {
        flap.registerAdapterDelegate(SimpleImageComponent2Delegate())
    }
}