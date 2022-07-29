package me.yifeiyuan.flapdev

import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.ktmodule.KtComponentModel
import me.yifeiyuan.flapdev.components.CustomViewTypeModel
import me.yifeiyuan.flapdev.components.TestBinderModel
import me.yifeiyuan.flapdev.components.databindingsample.SimpleDataBindingModel
import me.yifeiyuan.flapdev.components.generictest.GenericModel
import me.yifeiyuan.flapdev.components.SimpleImageModel
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import me.yifeiyuan.ktx.foundation.othermodule.JavaModuleModel
import me.yifeiyuan.ktx.foundation.othermodule.vb.ViewBindingModel
import java.util.*

/**
 * Created by 程序亦非猿 on 2021/9/28.
 */

fun FlapAdapter.mockData() {
    setData(mockMultiTypeModels())
}

fun FlapAdapter.appendMockData() {
    appendData(mockMultiTypeModels())
}

fun FlapAdapter.appendPrefetchData(size:Int = 10) {
    val list = ArrayList<Any>()
    repeat(size){
        list.add(SimpleTextModel("Prefetch $it of $size"))
    }
    appendData(list)
}

/**
 * 测试多类型数据
 */
fun mockMultiTypeModels(): MutableList<Any> {
    val models: MutableList<Any> = ArrayList()
    models.add(SimpleTextModel("Flap（灵动）"))
    models.add(SimpleTextModel("一个基于 RecyclerView 的页面组件化框架"))
    models.add(SimpleTextModel("—— by 程序亦非猿"))
    models.add(SimpleImageModel())
    models.add(CustomViewTypeModel())
    models.add(GenericModel())
    models.add(SimpleDataBindingModel())
    models.add(JavaModuleModel())
    models.add(KtComponentModel())
    models.add(ViewBindingModel())
    models.add(KtComponentModel())
    models.add(ViewBindingModel())
    models.add(TestBinderModel())
    models.add(KtComponentModel())
    models.add(TestBinderModel())
    return models
}