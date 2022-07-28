package me.yifeiyuan.flapdev.components.simpletext

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flapdev.FLogger
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2018/12/4.
 *
 * 作为文档 Demo
 */
//@Delegate(layoutName = "flap_item_simple_text")
@Delegate(layoutId = R.layout.flap_item_simple_text)
class SimpleTextComponent(itemView: View) : Component<SimpleTextModel>(itemView) {

    private val tvContent: TextView = findViewById(R.id.tv_content)

    override fun onBind(model: SimpleTextModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {
        FLogger.d(TAG, "onBind() called with: model = $model, position = $position, payloads = $payloads, adapter = $adapter")
        tvContent.text = model.content

        tvContent.setOnClickListener {
            val showToastEvent = Event("showToast","SimpleTextComponent fire event showToast"){
                Log.d(TAG, "onBind: showToastEvent success")
            }

            adapter.fireEvent(showToastEvent)

            val intE = Event("intEvent",3333)
            adapter.fireEvent(intE)
        }
    }

    override fun onBind(model: SimpleTextModel) {
        FLogger.d(TAG, "onBind() called with: model = $model")
    }

    companion object {
        private const val TAG = "SimpleTextItem"
    }
}

class SimpleTextComponentDelegate : AdapterDelegate<SimpleTextModel, SimpleTextComponent> {

    override fun delegate(model: Any): Boolean {
        return SimpleTextModel::class.java == model::class.java
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): SimpleTextComponent {
        return SimpleTextComponent(inflater.inflate(R.layout.flap_item_simple_text, parent, false))
    }

}