package me.yifeiyuan.flap.ktmodule

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.ktmodule.databinding.FlapItemVbBinding

/**
 *
 * 测试使用了 ViewBinding 的 Component
 * 使用 ViewBinding 的例子
 * Created by 程序亦非猿 on 2020/9/30.
 */

class ViewBindingModel

@Delegate(layoutName = "flap_item_vb", useViewBinding = true)
class ViewBindingComponent(var binding: FlapItemVbBinding) : Component<ViewBindingModel>(binding.root) {

    override fun onBind(model: ViewBindingModel) {
        binding.tvContent.text = "该 Component 使用了 ViewBinding"
    }

}