package me.yifeiyuan.flapdev

import androidx.recyclerview.widget.ItemTouchHelper
import me.yifeiyuan.flap.ext.SwipeDragHelper.Companion.FLAG_ALL_DIRECTIONS
import me.yifeiyuan.flap.ext.SwipeDragHelper.Companion.FLAG_LEFT_AND_RIGHT
import me.yifeiyuan.flap.ext.SwipeDragHelper.Companion.FLAG_UP_AND_DOWN
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
    models.addAll(mockFullFeatureModels())
    models.add(CustomViewTypeModel())
    models.add(SimpleDataBindingModel())
    models.add(KtComponentModel())
    models.add(ViewBindingModel())
    models.add(UnknownModel())
    return models
}

fun mockFullFeatureModels(): MutableList<Any> {
    val models: MutableList<Any> = ArrayList()

    val enabledModel = TestConfigModel().apply {
        clickEnable = true
        longClickEnable = true
        dragEnable = true
        swipeEnable = true
        swipeFlags = FLAG_LEFT_AND_RIGHT
        dragFlags = FLAG_ALL_DIRECTIONS
        title = "测试功能"
        content = "都可以"
    }

    models.add(enabledModel)

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


    val dragModel0 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = ItemTouchHelper.DOWN
        title = "测试拖动功能"
        content = "只可以向下拖动"
    }
    models.add(dragModel0)

    val dragModel1 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = ItemTouchHelper.UP
        title = "测试拖动功能"
        content = "只可以向上拖动"
    }
    models.add(dragModel1)

    val dragModel2 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = ItemTouchHelper.LEFT
        title = "测试拖动功能"
        content = "只可以向左拖动"
    }
    models.add(dragModel2)

    val dragModel3 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = ItemTouchHelper.RIGHT
        title = "测试拖动功能"
        content = "只可以向右拖动"
    }
    models.add(dragModel3)

    val dragModel4 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = FLAG_UP_AND_DOWN
        title = "测试拖动功能"
        content = "可以在【垂直方向】拖动"
    }
    models.add(dragModel4)

    val dragModel5 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = FLAG_LEFT_AND_RIGHT
        title = "测试拖动功能"
        content = "可以在【水平方向】拖动"
    }
    models.add(dragModel5)

    val dragModel6 = TestConfigModel().apply {
        dragEnable = true
        dragFlags = FLAG_ALL_DIRECTIONS
        title = "测试拖动功能"
        content = "可以在【全方向】拖动"
    }
    models.add(dragModel6)

    return models
}

//没有对应的 delegate 也没有 component，会有 fallback 机制兜底。
class UnknownModel


