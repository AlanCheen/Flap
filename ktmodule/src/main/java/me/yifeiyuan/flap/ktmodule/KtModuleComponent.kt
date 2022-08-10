package me.yifeiyuan.flap.ktmodule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate

/**
 * Created by 程序亦非猿 on 2020/9/30.
 */

class KtComponentModel

@Delegate(layoutName = "kt_module_component")
class KtModuleComponent(itemView: View) : Component<KtComponentModel>(itemView) {

    override fun onBind(model: KtComponentModel) {
    }
}

class KtModuleComponentDelegate : AdapterDelegate<KtComponentModel, KtModuleComponent> {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): KtModuleComponent {
        return KtModuleComponent(inflater.inflate(R.layout.kt_module_component, parent, false))
    }

    override fun delegate(model: Any): Boolean {
        return KtComponentModel::class.java == model::class.java
    }
}