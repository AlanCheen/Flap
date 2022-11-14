package me.yifeiyuan.flapdev.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.ViewTypeGenerator
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2018/12/4.
 *
 * 作为文档 Demo
 */

data class SimpleTextModel(val content: String) {
    override fun toString(): String {
        return "content=$content,SimpleTextModel"
    }
}

//@Delegate(layoutName = "flap_item_simple_text")
//@Delegate(layoutId = R.layout.flap_item_simple_text)
class SimpleTextComponent(itemView: View) : Component<SimpleTextModel>(itemView) {

    private val tvContent: TextView = findViewById(R.id.tv_content)

    override fun onBind(model: SimpleTextModel, position: Int, payloads: List<Any>) {
        tvContent.text = model.content
    }

    override fun onBind(model: SimpleTextModel) {
    }

}

//自定义 AdapterDelegate 实现
class SimpleTextComponentDelegate : AdapterDelegate<SimpleTextModel, SimpleTextComponent> {

    override fun delegate(model: Any): Boolean {
        return SimpleTextModel::class.java == model::class.java
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): SimpleTextComponent {
        return SimpleTextComponent(inflater.inflate(R.layout.flap_item_simple_text, parent, false))
    }

    override fun getItemViewType(model: Any): Int {
        return ViewTypeGenerator.generateViewType()
//        return R.layout.flap_item_simple_text
    }
}