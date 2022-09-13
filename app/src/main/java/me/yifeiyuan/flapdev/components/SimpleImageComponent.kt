package me.yifeiyuan.flapdev.components

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.R

/**
 * 简单的图片组件测试
 * Created by 程序亦非猿 on 2018/12/4.
 */

class SimpleImageModel {
    var url: String? = null
    var resId: Int = 0
}

@Delegate(layoutId = R.layout.component_simple_image)
//@Delegate(layoutName = "flap_item_simple_image")
class SimpleImageComponent(itemView: View) : Component<SimpleImageModel>(itemView) {

    private val imageView = findViewById<ImageView>(R.id.logo)

    override fun onBind(model: SimpleImageModel) {
        if (model.url?.isNotEmpty() == true) {
            Glide.with(context).load(model.url).into(imageView)
        } else if (model.resId > 0) {
            Glide.with(context).load(model.resId).into(imageView)
        }
    }
}