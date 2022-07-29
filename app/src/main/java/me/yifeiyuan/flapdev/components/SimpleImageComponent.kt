package me.yifeiyuan.flapdev.components

import android.view.View
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.R

/**
 * 简单的图片组件测试
 * Created by 程序亦非猿 on 2018/12/4.
 */

class SimpleImageModel

@Delegate(layoutId = R.layout.flap_item_simple_image)
//@Delegate(layoutName = "flap_item_simple_image")
class SimpleImageComponent(itemView: View) : Component<SimpleImageModel>(itemView) {
    override fun onBind(model: SimpleImageModel) {
    }
}