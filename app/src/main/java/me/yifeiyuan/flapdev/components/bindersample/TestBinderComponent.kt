package me.yifeiyuan.flapdev.components.bindersample

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flap.ext.bindView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2021/9/29.
 *
 * 利用 ktx 做 bind 逻辑，可以省去 findView 的操作
 *
 * @see ComponentBinder
 */

class TestBinderModel(val text: String = "Binder Sample")

@Delegate(layoutId = R.layout.flap_item_binder)
class TestBinderComponent(itemView: View) : Component<TestBinderModel>(itemView) {

    override fun onBind(model: TestBinderModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {
        bindTextView(R.id.binderText) {
            text = model.text
            setTextColor(Color.parseColor("#00ffff"))
        }

        bindButton(R.id.button) {
            setOnClickListener {
                Toast.makeText(it.context, "点击了 Button", Toast.LENGTH_SHORT).show()
            }
        }

        bindView<ImageView>(R.id.logo){
            setImageResource(R.mipmap.ic_launcher)
        }
    }

    override fun onBind(model: TestBinderModel) {
    }
}

//自定义实现
class TestBinderComponentDelegate : AdapterDelegate<TestBinderModel, TestBinderComponent> {

    override fun delegate(model: Any): Boolean {
        return TestBinderModel::class.java == model.javaClass
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): TestBinderComponent {
        return TestBinderComponent(inflater.inflate(R.layout.flap_item_binder, parent, false))
    }
}