package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapDebug

/**
 * FallbackAdapterDelegate is a build-in AdapterDelegate that would be used when something went wrong .
 * So that Flap won't crash your App.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
class FallbackAdapterDelegate : AdapterDelegate<Any, FallbackComponent> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): FallbackComponent {
        return FallbackComponent(TextView(parent.context))
    }

    override fun delegate(model: Any): Boolean {
        return true
    }

}

class FallbackComponent(v: View) : Component<Any>(v) {
    override fun onBind(model: Any) {
        if (FlapDebug.isDebug()) {
            (itemView as TextView).run {
                text = "$model 没有被正确代理，position = ${position},model = ${model}"
            }
        }
    }
}
