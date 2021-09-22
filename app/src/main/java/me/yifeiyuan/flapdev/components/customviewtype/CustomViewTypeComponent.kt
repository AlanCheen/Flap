package me.yifeiyuan.flapdev.components.customviewtype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.ComponentProxy
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.R

/**
 * 完全自定义 ComponentProxy 用例。
 *
 * Created by 程序亦非猿 on 2019/1/18.
 */
class CustomViewTypeComponent(itemView: View) : Component<CustomModel>(itemView) {

    override fun onBind(model: CustomModel, position: Int, adapter: FlapAdapter) {}

    class Factory : ComponentProxy<CustomModel, CustomViewTypeComponent> {
        override fun createComponent(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): CustomViewTypeComponent {
            return CustomViewTypeComponent(inflater.inflate(R.layout.flap_item_custom_type, parent, false))
        }

        override fun getItemViewType(model: CustomModel?): Int {
            return CUSTOM_ITEM_VIEW_TYPE
        }

        override fun getComponentModelClass(): Class<CustomModel> {
            return CustomModel::class.java
        }
    }

    companion object {
        const val CUSTOM_ITEM_VIEW_TYPE = 466
    }
}

class CustomViewTypeComponentDelegate : AdapterDelegate<CustomModel, CustomViewTypeComponent> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): CustomViewTypeComponent {
        return CustomViewTypeComponent(inflater.inflate(R.layout.flap_item_custom_type, parent, false))
    }

    override fun getItemViewType(model: Any, position: Int): Int {
        return CustomViewTypeComponent.CUSTOM_ITEM_VIEW_TYPE
    }
}