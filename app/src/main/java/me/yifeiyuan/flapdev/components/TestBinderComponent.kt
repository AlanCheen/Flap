package me.yifeiyuan.flapdev.components

import android.graphics.Color
import android.view.View
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
 * 测试 ComponentBinder 相关 bindXXX 的方法。
 *
 */

class TestBinderModel(val text: String = "ComponentBinder Sample")

@Delegate(layoutId = R.layout.flap_item_binder)
class TestBinderComponent(itemView: View) : Component<TestBinderModel>(itemView) {

    override fun onBind(model: TestBinderModel) {
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
}