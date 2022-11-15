package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.dsl.databinding.adapterDelegateDataBinding
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.databinding.FlapItemSimpleDatabindingBinding

/**
 * 测试 DataBinding 功能
 *
 * Created by 程序亦非猿 on 2022/8/9.
 */

class SimpleDataBindingModel {
    var text = "使用 DataBinding 的 Component"
}

//DSL 的方式
fun createDataBindingDelegate() = adapterDelegateDataBinding<SimpleDataBindingModel, FlapItemSimpleDatabindingBinding>(R.layout.flap_item_simple_databinding) {
    onBind { model ->
        binding.model = model
        binding.executePendingBindings()
    }
}