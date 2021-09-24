package me.yifeiyuan.flapdev.components.simpletext

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
//@Proxy(layoutId = R.layout.flap_item_simple_text,layoutName = "flap_item_simple_text")
@Delegate(layoutName = "flap_item_simple_text")
class SimpleTextComponent(itemView: View) : Component<SimpleTextModel>(itemView) {

    private val tvContent: TextView = findViewById(R.id.tv_content)

    override fun onBind(model: SimpleTextModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {
//        super.onBind(model, position, payloads, adapter)
        Log.d(TAG, "onBind() called with: model = $model, position = $position, payloads = $payloads, adapter = $adapter")
        tvContent.text = model.content
    }

    companion object {
        private const val TAG = "SimpleTextItem"
    }

    override fun onBind(model: SimpleTextModel) {
        Log.d(TAG, "onBind() called with: model = $model")
        position
    }

}

class SimpleTextComponentDelegate : AdapterDelegate<SimpleTextModel, SimpleTextComponent> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): SimpleTextComponent {
        return SimpleTextComponent(inflater.inflate(R.layout.flap_item_simple_text, parent, false))
    }

}