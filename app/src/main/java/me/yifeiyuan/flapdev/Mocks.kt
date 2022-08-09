package me.yifeiyuan.flapdev

import me.yifeiyuan.flap.ktmodule.KtComponentModel
import me.yifeiyuan.flapdev.components.*
import me.yifeiyuan.flapdev.components.databindingsample.SimpleDataBindingModel
import me.yifeiyuan.flapdev.components.generictest.GenericModel
import me.yifeiyuan.ktx.foundation.othermodule.JavaModuleModel
import me.yifeiyuan.ktx.foundation.othermodule.vb.ViewBindingModel
import java.util.*

/**
 * Created by 程序亦非猿 on 2021/9/28.
 */

/**
 * 测试多类型数据
 */
fun mockMultiTypeModels(): MutableList<Any> {
    val models: MutableList<Any> = ArrayList()
    models.add(SimpleTextModel("Flap（灵动）"))
    models.add(SimpleTextModel("一个基于 RecyclerView 的页面组件化框架"))
    models.add(SimpleTextModel("—— by 程序亦非猿"))
    models.add(SimpleImageModel())
    models.add(TestAllModel())
    models.add(CustomViewTypeModel())
    models.add(GenericModel())
    models.add(SimpleDataBindingModel())
    models.add(JavaModuleModel())
    models.add(KtComponentModel())
    models.add(ViewBindingModel())
    models.add(KtComponentModel())
    models.add(TestBinderModel())
    models.add(UnknownModel())
    return models
}

//没有对应的 delegate 也没有 component，会有 fallback 机制兜底。
class UnknownModel


