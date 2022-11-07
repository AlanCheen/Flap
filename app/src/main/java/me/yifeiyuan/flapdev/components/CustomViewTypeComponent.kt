package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R

/**
 * 完全自定义 AdapterDelegate 用例，使用了自定义 ItemViewType
 *
 * Created by 程序亦非猿 on 2019/1/18.
 */

class CustomViewTypeModel(var content: String = "自定义 itemViewType 的 Component")

const val CUSTOM_ITEM_VIEW_TYPE = 466

fun createCustomViewTypeComponentDelegate() = adapterDelegate<CustomViewTypeModel>(R.layout.flap_item_custom_type, itemViewType = CUSTOM_ITEM_VIEW_TYPE) {
    onBind { model, position, payloads ->
        bindTextView(R.id.tv_content) {
            text = model.content
        }
    }
}