package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R

/**
 *
 * Created by 程序亦非猿 on 2022/7/27.
 */

class TestClickModel(var content: String)

fun createTestClickDelegate() = adapterDelegate<TestClickModel>(R.layout.component_test_click) {
    onBind { model, position, payloads, adapter ->
        bindTextView(R.id.clicks){
            text = model.content
        }
    }
}