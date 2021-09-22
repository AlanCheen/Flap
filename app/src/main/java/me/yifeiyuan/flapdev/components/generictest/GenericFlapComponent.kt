package me.yifeiyuan.flapdev.components.generictest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Proxy
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.base.BaseFlapComponent

/**
 * Created by 程序亦非猿 on 2019/1/29.
 */
//@Proxy(layoutId = R.layout.flap_item_generic_type)
@Proxy(layoutName = "flap_item_generic_type")
class GenericFlapComponent(itemView: View) : BaseFlapComponent<GenericModel>(itemView) {
    override fun onBind(model: GenericModel, position: Int, adapter: FlapAdapter) {}
}

class GenericFlapComponentDelegate : AdapterDelegate<GenericModel, GenericFlapComponent> {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): GenericFlapComponent {
        return GenericFlapComponent(inflater.inflate(R.layout.flap_item_generic_type,parent,false))
    }

}