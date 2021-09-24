package me.yifeiyuan.flap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * DefaultAdapterDelegate is a build-in AdapterDelegate that would be used when something went wrong .
 * So that Flap won't crash your App.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
class DefaultAdapterDelegate : AdapterDelegate<Any, DefaultComponent> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): DefaultComponent {
        return DefaultComponent(TextView(parent.context))
    }

}

class DefaultComponent(v: View) : Component<Any>(v) {
    override fun onBind(model: Any) {
        (itemView as TextView).run {
            text = "$model 没有被正确代理，position = ${position}"
        }
    }

}
