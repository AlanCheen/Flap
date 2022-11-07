package me.yifeiyuan.flapdev.components

import com.bumptech.glide.Glide
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.ext.bindImageView
import me.yifeiyuan.flapdev.R

/**
 * 简单的图片组件测试
 * Created by 程序亦非猿 on 2018/12/4.
 */

class SimpleImageModel {
    var url: String? = null
    var resId: Int = 0
}

fun createSimpleImageDelegate() = adapterDelegate<SimpleImageModel>(R.layout.component_simple_image) {
    onBind { model, position, payloads ->
        bindImageView(R.id.logo) {
            if (model.url?.isNotEmpty() == true) {
                Glide.with(context).load(model.url).into(this)
            } else if (model.resId > 0) {
                Glide.with(context).load(model.resId).into(this)
            }
        }
    }
}