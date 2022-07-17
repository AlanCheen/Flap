package me.yifeiyuan.flapdev

import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.ktmodule.KtComponentModel
import me.yifeiyuan.flapdev.components.bindersample.ViewBinderModel
import me.yifeiyuan.flapdev.components.customviewtype.CustomViewTypeModel
import me.yifeiyuan.flapdev.components.databindingsample.SimpleDataBindingModel
import me.yifeiyuan.flapdev.components.generictest.GenericModel
import me.yifeiyuan.flapdev.components.simpleimage.SimpleImageModel
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import me.yifeiyuan.ktx.foundation.othermodule.JavaModuleModel
import me.yifeiyuan.ktx.foundation.othermodule.vb.VBModel
import java.util.*

/**
 * Created by 程序亦非猿 on 2021/9/28.
 */

fun FlapAdapter.mockData() {
    setData(mockModels())
}

fun FlapAdapter.appendMockData() {
    appendData(mockModels())
}

fun FlapAdapter.appendPrefetchData(size:Int = 10) {
    val list = ArrayList<Any>()
    repeat(size){
        list.add(SimpleTextModel("Prefetch $it of $size"))
    }
    appendData(list)
}

fun mockModels(): MutableList<Any> {
    val models: MutableList<Any> = ArrayList()
    models.add(SimpleTextModel("Flap（灵动）"))
    models.add(SimpleTextModel("一个基于 RecyclerView 的页面组件化框架"))
    models.add(SimpleTextModel("—— by 程序亦非猿"))
    models.add(SimpleImageModel())
    models.add(CustomViewTypeModel())
    models.add(GenericModel())
    models.add(GenericModel())
    models.add(SimpleImageModel())
    models.add(GenericModel())
    models.add(SimpleDataBindingModel())
    models.add(SimpleDataBindingModel())
    models.add(JavaModuleModel())
    models.add(JavaModuleModel())
    models.add(KtComponentModel())
    models.add(VBModel())
    models.add(KtComponentModel())
    models.add(VBModel())
    models.add(ViewBinderModel())
    models.add(KtComponentModel())
    models.add(ViewBinderModel())
    models.add(ViewBinderModel())
    return models
}