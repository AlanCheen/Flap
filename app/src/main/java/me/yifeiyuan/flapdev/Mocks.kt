package me.yifeiyuan.flapdev

import androidx.recyclerview.widget.ItemTouchHelper
import me.yifeiyuan.flap.ktmodule.KtComponentModel
import me.yifeiyuan.flapdev.components.*
import me.yifeiyuan.flapdev.components.SimpleDataBindingModel
import me.yifeiyuan.flapdev.components.ViewBindingModel
import java.util.*

/**
 * Created by 程序亦非猿 on 2021/9/28.
 */

/**
 * 测试多类型数据
 */
fun mockMultiTypeModels(): MutableList<Any> {
    val models: MutableList<Any> = ArrayList()

    val bannerModel = BannerModel()

    bannerModel.images = mutableListOf<BannerImageModel>().apply {
        add(BannerImageModel().apply { resId = R.drawable.flap_ic_baicai })
        add(BannerImageModel().apply { resId = R.drawable.flap_ic_baozi })
        add(BannerImageModel().apply { resId = R.drawable.flap_ic_jd })
    }
    models.add(SimpleImageModel())
    models.add(SimpleTextModel("Flap（灵动），一个基于 RecyclerView 的页面组件化框架。 by 程序亦非猿"))
    models.add(bannerModel)
    models.add(TestAdapterApiModel())
    models.add(CustomViewTypeModel())
    models.add(SimpleDataBindingModel())
    models.add(KtComponentModel())
    models.add(ViewBindingModel())
    models.add(TestBinderModel())
    models.add(UnknownModel())
    models.addAll(mockFullFeatureModels())
    return models
}

fun mockFullFeatureModels(): MutableList<Any> {
    val models: MutableList<Any> = ArrayList()

    val clickModel = TestConfigModel().apply {
        clickEnable = true
        title = "测试点击功能"
        content = "可以点击"
    }
    val clickModel2 = TestConfigModel().apply {
        clickEnable = false
        title = "测试点击功能"
        content = "不可点击"
    }
    models.add(clickModel)
    models.add(clickModel2)

    val longClickModel = TestConfigModel().apply {
        longClickEnable = true
        title = "测试长按功能"
        content = "可以长按"
    }
    val longClickModel2 = TestConfigModel().apply {
        longClickEnable = false
        title = "测试长按功能"
        content = "不可长按"
    }
    models.add(longClickModel)
    models.add(longClickModel2)

    val swipeModel = TestConfigModel().apply {
        swipeEnable = true
        title = "测试滑动删除功能"
        content = "可 从右往左 滑动删除"
        swipeFlags = ItemTouchHelper.LEFT
    }
    val swipeModel2 = TestConfigModel().apply {
        swipeEnable = true
        title = "测试滑动删除功能"
        content = "可 从左往右 滑动删除"
        swipeFlags = ItemTouchHelper.RIGHT
    }
    models.add(swipeModel)
    models.add(swipeModel2)

    return models
}

//没有对应的 delegate 也没有 component，会有 fallback 机制兜底。
class UnknownModel


