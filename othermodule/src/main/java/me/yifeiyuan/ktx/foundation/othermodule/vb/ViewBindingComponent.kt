package me.yifeiyuan.ktx.foundation.othermodule.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Proxy
import me.yifeiyuan.ktx.foundation.othermodule.databinding.FlapItemVbBinding

/**
 * 使用 ViewBinding 的例子
 * Created by 程序亦非猿 on 2020/9/30.
 */
@Proxy(layoutName = "flap_item_vb", useViewBinding = true)
class ViewBindingComponent(var binding: FlapItemVbBinding) : Component<VBModel>(binding.root) {

    override fun onBind(model: VBModel, position: Int, adapter: FlapAdapter) {
        binding.tvContent.text = "ViewBinding Sample"
    }

}

class ViewBindingComponentDelegate : AdapterDelegate<VBModel, ViewBindingComponent> {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewBindingComponent {
        return ViewBindingComponent(FlapItemVbBinding.inflate(inflater, parent, false))
    }
}