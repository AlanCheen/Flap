package me.yifeiyuan.ktx.foundation.othermodule.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.ktx.foundation.othermodule.databinding.FlapItemVbBinding

/**
 * 使用 ViewBinding 的例子
 * Created by 程序亦非猿 on 2020/9/30.
 */
@Delegate(layoutName = "flap_item_vb", useViewBinding = true)
class ViewBindingComponent(var binding: FlapItemVbBinding) : Component<ViewBindingModel>(binding.root) {

    override fun onBind(model: ViewBindingModel) {
        binding.tvContent.text = "ViewBinding Sample"
    }

}

class ViewBindingComponentDelegate : AdapterDelegate<ViewBindingModel, ViewBindingComponent> {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewBindingComponent {
        return ViewBindingComponent(FlapItemVbBinding.inflate(inflater, parent, false))
    }

    override fun delegate(model: Any): Boolean {
        return ViewBindingModel::class.java == model::class.java
    }
}