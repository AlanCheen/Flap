package me.yifeiyuan.flapdev.components.bindersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2021/9/29.
 *
 * 利用 ktx 做 bind 逻辑，可以省去 findView 的操作
 */
@Delegate
class ViewBinderComponent(itemView: View) : Component<ViewBinderModel>(itemView) {

    override fun onBind(model: ViewBinderModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {

        bindTextView(R.id.binderText) {
            text = model.text
        }

        bindButton(R.id.button) {
            setOnClickListener {
                Toast.makeText(it.context,"点击了 Button",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBind(model: ViewBinderModel) {
    }

}

class BinderComponentDelegate : AdapterDelegate<ViewBinderModel, ViewBinderComponent> {

    override fun delegate(model: Any): Boolean {
        return ViewBinderModel::class.java == model.javaClass
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewBinderComponent {
        return ViewBinderComponent(inflater.inflate(R.layout.flap_item_binder, parent, false))
    }
}