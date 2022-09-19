package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.differ.IDiffer
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2022/9/13.
 */

class TestConfigModel : IDiffer {

    var id: Int = -1
    var title: String? = null
    var content: String? = null

    var dragEnable: Boolean = false
    var swipeEnable: Boolean = false

    var swipeFlags: Int = 0
    var dragFlags: Int = 0

    var clickEnable: Boolean = false
    var longClickEnable: Boolean = false

    override fun areItemsTheSame(newItem: Any): Boolean {
        return false
    }

    override fun areContentsTheSame(newItem: Any): Boolean {
        return false
    }

    override fun getChangePayload(newItem: Any): Any? {
        return super.getChangePayload(newItem)
    }
}

fun fullConfigAdapterDelegate() = adapterDelegate<TestConfigModel>(R.layout.component_full_feature) {

    onBind { model, position, payloads, adapter ->

        bindTextView(R.id.title) {
            text = model.title
        }

        bindTextView(R.id.content) {
            text = model.content
        }

        swipeEnable = model.swipeEnable
        dragEnable = model.dragEnable
        swipeFlags = model.swipeFlags
        dragFlags = model.dragFlags
        clickable = model.clickEnable
        longClickable = model.longClickEnable
    }
}