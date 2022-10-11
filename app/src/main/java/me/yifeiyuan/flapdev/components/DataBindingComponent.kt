package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.dsl.databinding.adapterDelegateDataBinding
import me.yifeiyuan.flapdev.R
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
class DataBindingComponent(private var binding: FlapItemSimpleDatabindingBinding) : Component<SimpleDataBindingModel>(binding.root) {

    override fun onBind(model: SimpleDataBindingModel) {
        binding.model = model
        binding.executePendingBindings()
    }

}

fun createDataBindingDelegate() = adapterDelegateDataBinding<SimpleDataBindingModel, FlapItemSimpleDatabindingBinding>(R.layout.flap_item_simple_databinding) {
    onBind { model ->
        binding.model = model
        binding.executePendingBindings()
    }
}