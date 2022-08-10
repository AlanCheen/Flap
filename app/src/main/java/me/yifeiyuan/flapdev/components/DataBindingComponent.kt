package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.databinding.FlapItemSimpleDatabindingBinding

/**
 * 测试 DataBinding 功能
 *
 *
 * Created by 程序亦非猿 on 2022/8/9.
 */

class SimpleDataBindingModel {
    var text = "该 Component 使用了 DataBinding"
}

@Delegate(layoutName = "flap_item_simple_databinding", useDataBinding = true)
class DataBindingComponent(private var binding: FlapItemSimpleDatabindingBinding) : Component<SimpleDataBindingModel?>(binding.getRoot()) {

    override fun onBind(model: SimpleDataBindingModel?) {
        binding.model = model
        binding.executePendingBindings()
    }

}