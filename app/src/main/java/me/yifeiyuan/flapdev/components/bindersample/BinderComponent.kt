package me.yifeiyuan.flapdev.components.bindersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flap.ext.bindView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2021/9/29.
 */
@Delegate
class BinderComponent(itemView: View) : Component<BinderModel>(itemView) {

    override fun onBind(model: BinderModel) {

        bindTextView(R.id.binderText) {
            text = model.text
        }
//                .bindTextView(R.id.binderText) {}
//                .bindImageView(R.id.binderText){}
    }

}

class BinderComponentDelegate : AdapterDelegate<BinderModel, BinderComponent> {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BinderComponent {
        return BinderComponent(inflater.inflate(R.layout.flap_item_binder, parent, false))
    }
}