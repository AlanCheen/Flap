package me.yifeiyuan.flap.ktmodule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Proxy

/**
 * Created by 程序亦非猿 on 2020/9/30.
 */
@Proxy(layoutName = "kt_module_component")
class KtModuleComponent(itemView: View) : Component<KtComponentModel>(itemView) {

    override fun onBind(model: KtComponentModel, position: Int, adapter: FlapAdapter) {
    }
}

class KtModuleComponentDelegate :AdapterDelegate<KtComponentModel,KtModuleComponent>{
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): KtModuleComponent {
        return KtModuleComponent(inflater.inflate(R.layout.kt_module_component,parent,false))
    }

}