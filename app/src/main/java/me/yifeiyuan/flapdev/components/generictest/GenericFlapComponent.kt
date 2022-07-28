package me.yifeiyuan.flapdev.components.generictest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.base.BaseFlapComponent

/**
 * Created by 程序亦非猿 on 2019/1/29.
 */
@Delegate(layoutName = "flap_item_generic_type")
class GenericFlapComponent(itemView: View) : BaseFlapComponent<GenericModel>(itemView) {
    override fun onBind(model: GenericModel) {
    }
}

class GenericFlapComponentDelegate : AdapterDelegate<GenericModel, GenericFlapComponent> {

    override fun delegate(model: Any): Boolean {
        return GenericModel::class.java == model.javaClass
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): GenericFlapComponent {
        return GenericFlapComponent(inflater.inflate(R.layout.flap_item_generic_type,parent,false))
    }

}