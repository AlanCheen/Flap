package me.yifeiyuan.flapdev.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flapdev.R

/**
 * 完全自定义 AdapterDelegate 用例，使用了自定义 ItemViewType
 *
 * Created by 程序亦非猿 on 2019/1/18.
 */

class CustomViewTypeModel

class CustomViewTypeComponent(itemView: View) : Component<CustomViewTypeModel>(itemView) {

    companion object {
        const val CUSTOM_ITEM_VIEW_TYPE = 466
    }

    override fun onBind(model: CustomViewTypeModel) {
    }
}

class CustomViewTypeComponentDelegate : AdapterDelegate<CustomViewTypeModel, CustomViewTypeComponent> {

    override fun delegate(model: Any): Boolean {
        return CustomViewTypeModel::class.java == model.javaClass
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): CustomViewTypeComponent {
        return CustomViewTypeComponent(inflater.inflate(R.layout.flap_item_custom_type, parent, false))
    }

    override fun getItemViewType(model: Any): Int {
        return CustomViewTypeComponent.CUSTOM_ITEM_VIEW_TYPE
    }
}