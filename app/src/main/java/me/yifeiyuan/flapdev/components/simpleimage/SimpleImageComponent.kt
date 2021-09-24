package me.yifeiyuan.flapdev.components.simpleimage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
//@Proxy(layoutId = R.layout.flap_item_simple_image)
@Delegate(layoutName = "flap_item_simple_image")
class SimpleImageComponent(itemView: View) : Component<SimpleImageModel>(itemView) {
    override fun onBind(model: SimpleImageModel) {
    }
}

class SimpleImageComponentDelegate : AdapterDelegate<SimpleImageModel, SimpleImageComponent> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): SimpleImageComponent {
        return SimpleImageComponent(inflater.inflate(R.layout.flap_item_simple_image, parent, false))
    }

}