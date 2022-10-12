package me.yifeiyuan.flap.ktmodule

import android.view.View
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